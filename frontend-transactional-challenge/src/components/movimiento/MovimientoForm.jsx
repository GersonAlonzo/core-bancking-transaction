import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { movimientoService, cuentaService } from '../../services/apiServices';

const MovimientoForm = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [cuentas, setCuentas] = useState([]);
  const [searchTermOrigen, setSearchTermOrigen] = useState('');
  const [searchTermDestino, setSearchTermDestino] = useState('');
  const [showDropdownOrigen, setShowDropdownOrigen] = useState(false);
  const [showDropdownDestino, setShowDropdownDestino] = useState(false);
  const [apiError, setApiError] = useState(''); 

  const {
    register,
    handleSubmit,
    formState: { errors },
    watch,
    setValue,
    getValues
  } = useForm({
    defaultValues: {
      cuentaOrigen: '',
      cuentaDestino: '',
      tipoMovimiento: 'DEPOSITO',
      monto: ''
    }
  });

  const currentTipoMovimiento = watch('tipoMovimiento');

  useEffect(() => {
    const loadCuentas = async () => {
      try {
        const response = await cuentaService.getAll();
        setCuentas(response.data);
      } catch (error) {
        console.error('Error al cargar cuentas:', error);
        setCuentas([]);
      }
    };

    loadCuentas();
  }, []);

  useEffect(() => {
    if (currentTipoMovimiento === 'DEPOSITO') {
      setValue('cuentaOrigen', '');
      setSearchTermOrigen('');
    } else if (currentTipoMovimiento === 'RETIRO') {
      setValue('cuentaDestino', '');
      setSearchTermDestino('');
    }
    setApiError('');
  }, [currentTipoMovimiento, setValue]);

  const filteredCuentasOrigen = cuentas.filter(cuenta => 
    cuenta.numeroCuenta.toLowerCase().includes(searchTermOrigen.toLowerCase())
  );

  const filteredCuentasDestino = cuentas.filter(cuenta => {
    const matchesSearch = cuenta.numeroCuenta.toLowerCase().includes(searchTermDestino.toLowerCase());
    

    if (currentTipoMovimiento === 'TRANSFER') {
      const cuentaOrigenValue = getValues('cuentaOrigen');
      return matchesSearch && cuenta.numeroCuenta !== cuentaOrigenValue;
    }
    
    return matchesSearch;
  });

  const handleSelectCuentaOrigen = (cuenta) => {
    setValue('cuentaOrigen', cuenta.numeroCuenta, { shouldValidate: true });
    setSearchTermOrigen(cuenta.numeroCuenta);
    setShowDropdownOrigen(false);
    setApiError(''); 
  };

  const handleSelectCuentaDestino = (cuenta) => {
    setValue('cuentaDestino', cuenta.numeroCuenta, { shouldValidate: true });
    setSearchTermDestino(cuenta.numeroCuenta);
    setShowDropdownDestino(false);
    setApiError(''); 
  };

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (!event.target.closest('.dropdown-container-origen')) {
        setShowDropdownOrigen(false);
      }
      if (!event.target.closest('.dropdown-container-destino')) {
        setShowDropdownDestino(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);

  const parseErrorMessage = (error) => {
    const errorMessage = error.response?.data?.mensaje || 'No se pudo procesar el movimiento';
    
    if (errorMessage.includes('saldo insuficiente') || 
        errorMessage.includes('insuficiente') || 
        errorMessage.includes('no cuenta con fondos')) {
      return 'Saldo insuficiente para realizar esta operación';
    }
    
    return errorMessage;
  };

  const onSubmit = async (data) => {
    try {
      setLoading(true);
      setApiError(''); 
      
      let datosMovimiento = {
        tipoMovimiento: data.tipoMovimiento,
        monto: parseFloat(data.monto)
      };

      if (data.tipoMovimiento === 'DEPOSITO') {
        datosMovimiento.cuentaDestino = data.cuentaDestino;
      } else if (data.tipoMovimiento === 'RETIRO') {
        datosMovimiento.cuentaOrigen = data.cuentaOrigen;
      } else if (data.tipoMovimiento === 'TRANSFER') {
        datosMovimiento.cuentaOrigen = data.cuentaOrigen;
        datosMovimiento.cuentaDestino = data.cuentaDestino;
      }

      const response = await movimientoService.create(datosMovimiento);
      
      alert(response.data.mensaje || 'Movimiento registrado con éxito');
      
      navigate('/movimientos');
    } catch (error) {
      console.error('Error al registrar movimiento:', error);
      
      const errorMessage = parseErrorMessage(error);
      setApiError(errorMessage);
      
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="w-full">
      <h2 className="text-xl font-semibold mb-6">
        Registrar Nuevo Movimiento
      </h2>
      
      {/* Mensaje de error de la API */}
      {apiError && (
        <div className="mb-4 p-3 bg-red-50 border border-red-300 rounded-md">
          <p className="text-red-600 font-medium">{apiError}</p>
        </div>
      )}
      
      <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
        {/* Campo Tipo de Movimiento */}
        <div className="form-group">
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Tipo de Movimiento
          </label>
          <select
            className={`w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 ${errors.tipoMovimiento ? 'border-red-500' : ''}`}
            {...register('tipoMovimiento', { 
              required: 'Debe seleccionar un tipo de movimiento'
            })}
            onChange={(e) => {
              setValue('tipoMovimiento', e.target.value);
              setApiError(''); // Limpiar error al cambiar tipo
            }}
          >
            <option value="DEPOSITO">Depósito</option>
            <option value="RETIRO">Retiro</option>
            <option value="TRANSFER">Transferencia entre cuentas</option>
          </select>
          {errors.tipoMovimiento && (
            <p className="mt-1 text-sm text-red-600">{errors.tipoMovimiento.message}</p>
          )}
        </div>
        
        {/* Campo Cuenta Origen (solo para Retiro y TRANSFER) */}
        {(currentTipoMovimiento === 'RETIRO' || currentTipoMovimiento === 'TRANSFER') && (
          <div className="form-group">
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Cuenta Origen
            </label>
            <div className="dropdown-container-origen relative">
              <input
                type="text"
                className={`w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 ${errors.cuentaOrigen || (apiError && currentTipoMovimiento === 'RETIRO') ? 'border-red-500' : ''}`}
                placeholder="Buscar cuenta origen..."
                value={searchTermOrigen}
                onChange={(e) => {
                  setSearchTermOrigen(e.target.value);
                  setValue('cuentaOrigen', e.target.value);
                  setShowDropdownOrigen(true);
                  setApiError(''); // Limpiar error al cambiar cuenta
                }}
                onFocus={() => setShowDropdownOrigen(true)}
              />
              <input type="hidden" {...register('cuentaOrigen', { required: 'Debe seleccionar una cuenta origen' })} />
              
              {showDropdownOrigen && filteredCuentasOrigen.length > 0 && (
                <div className="absolute z-10 mt-1 w-full bg-white border border-gray-300 rounded-md shadow-lg max-h-60 overflow-auto">
                  {filteredCuentasOrigen.map(cuenta => (
                    <div
                      key={cuenta.numeroCuenta}
                      className="px-4 py-2 hover:bg-gray-100 cursor-pointer flex justify-between"
                      onClick={() => handleSelectCuentaOrigen(cuenta)}
                    >
                      <span>{cuenta.numeroCuenta}</span>
                      <span className="text-gray-600">Saldo: ${cuenta.saldo?.toFixed(2) || '0.00'}</span>
                    </div>
                  ))}
                </div>
              )}
            </div>
            {errors.cuentaOrigen && (
              <p className="mt-1 text-sm text-red-600">{errors.cuentaOrigen.message}</p>
            )}
          </div>
        )}
        
        {/* Campo Cuenta Destino (solo para Depósito y TRANSFER) */}
        {(currentTipoMovimiento === 'DEPOSITO' || currentTipoMovimiento === 'TRANSFER') && (
          <div className="form-group">
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Cuenta Destino
            </label>
            <div className="dropdown-container-destino relative">
              <input
                type="text"
                className={`w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 ${errors.cuentaDestino || (apiError && currentTipoMovimiento === 'DEPOSITO') ? 'border-red-500' : ''}`}
                placeholder="Buscar cuenta destino..."
                value={searchTermDestino}
                onChange={(e) => {
                  setSearchTermDestino(e.target.value);
                  setValue('cuentaDestino', e.target.value);
                  setShowDropdownDestino(true);
                  setApiError(''); // Limpiar error al cambiar cuenta
                }}
                onFocus={() => setShowDropdownDestino(true)}
              />
              <input type="hidden" {...register('cuentaDestino', { required: 'Debe seleccionar una cuenta destino' })} />
              
              {showDropdownDestino && filteredCuentasDestino.length > 0 && (
                <div className="absolute z-10 mt-1 w-full bg-white border border-gray-300 rounded-md shadow-lg max-h-60 overflow-auto">
                  {filteredCuentasDestino.map(cuenta => (
                    <div
                      key={cuenta.numeroCuenta}
                      className="px-4 py-2 hover:bg-gray-100 cursor-pointer flex justify-between"
                      onClick={() => handleSelectCuentaDestino(cuenta)}
                    >
                      <span>{cuenta.numeroCuenta}</span>
                      <span className="text-gray-600">Saldo: ${cuenta.saldo?.toFixed(2) || '0.00'}</span>
                    </div>
                  ))}
                </div>
              )}
            </div>
            {errors.cuentaDestino && (
              <p className="mt-1 text-sm text-red-600">{errors.cuentaDestino.message}</p>
            )}
          </div>
        )}
        
        {/* Campo Monto */}
        <div className="form-group">
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Monto
          </label>
          <input
            type="number"
            step="0.01"
            min="0.01"
            className={`w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 ${errors.monto || apiError ? 'border-red-500' : ''}`}
            {...register('monto', { 
              required: 'El monto es obligatorio',
              min: {
                value: 0.01,
                message: 'El monto debe ser mayor a cero'
              }
            })}
            onChange={(e) => {
              setValue('monto', e.target.value);
              setApiError(''); // Limpiar error al cambiar monto
            }}
          />
          {errors.monto && (
            <p className="mt-1 text-sm text-red-600">{errors.monto.message}</p>
          )}
        </div>
        
        {/* Botones */}
        <div className="flex justify-end space-x-3 pt-4">
          <button
            type="button"
            onClick={() => navigate('/movimientos')}
            className="px-4 py-2 bg-gray-200 text-gray-800 rounded-md hover:bg-gray-300 transition-colors"
            disabled={loading}
          >
            Cancelar
          </button>
          <button
            type="submit"
            className="px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 transition-colors focus:outline-none focus:ring-2 focus:ring-blue-300"
            disabled={loading}
          >
            {loading ? 'Procesando...' : 'Procesar Movimiento'}
          </button>
        </div>
      </form>
    </div>
  );
};

export default MovimientoForm;