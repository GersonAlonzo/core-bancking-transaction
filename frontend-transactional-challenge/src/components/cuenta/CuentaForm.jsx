import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { cuentaService, clienteService } from '../../services/apiServices';

const CuentaForm = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [clientes, setClientes] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [showDropdown, setShowDropdown] = useState(false);
  const [apiError, setApiError] = useState('');

  const {
    register,
    handleSubmit,
    formState: { errors },
    setValue,
    watch
  } = useForm({
    defaultValues: {
      clienteId: '',
      clienteNombre: '',
      saldo: 0
    }
  });

  const watchedClienteId = watch('clienteId');
  const watchedClienteNombre = watch('clienteNombre');

  // Cargar lista de clientes
  useEffect(() => {
    const loadClientes = async () => {
      try {
        const response = await clienteService.getAll();
        setClientes(response.data);
      } catch (error) {
        console.error('Error al cargar clientes:', error);
        setApiError('No se pudieron cargar los clientes. Por favor, intente más tarde.');
      }
    };

    loadClientes();
  }, []);

  const filteredClientes = clientes.filter(cliente => 
    cliente.nombre.toLowerCase().includes(searchTerm.toLowerCase()) ||
    cliente.identificacion.toLowerCase().includes(searchTerm.toLowerCase())
  );


  const handleSelectCliente = (cliente) => {
    setValue('clienteId', cliente.id, { shouldValidate: true });
    setValue('clienteNombre', `${cliente.nombre} - ${cliente.identificacion}`, { shouldValidate: true });
    setSearchTerm(`${cliente.nombre} - ${cliente.identificacion}`);
    setShowDropdown(false);
    setApiError(''); 
  };

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (!event.target.closest('.dropdown-container')) {
        setShowDropdown(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);

  useEffect(() => {
    if (!watchedClienteNombre) {
      setValue('clienteId', '');
    }
  }, [watchedClienteNombre, setValue]);

  const onSubmit = async (data) => {
    try {
      setLoading(true);
      setApiError(''); 
      
      await cuentaService.create({
        numeroCuenta: '00',
        clienteId: data.clienteId,
        saldo: parseFloat(data.saldo),
        estadoCuenta:"null",
      });
      
      navigate('/cuentas');
    } catch (error) {
      console.error('Error al guardar cuenta:', error);
      
      const errorMessage = error.response?.data?.mensaje || 'No se pudo guardar la cuenta';
      setApiError(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="w-full">
      <h2 className="text-xl font-semibold mb-6">
        Registrar Nueva Cuenta
      </h2>
      
      {/* Mensaje de error de la API */}
      {apiError && (
        <div className="mb-4 p-3 bg-red-50 border border-red-300 rounded-md">
          <p className="text-red-600 font-medium">{apiError}</p>
        </div>
      )}
      
      <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
        {/* Campo Cliente con búsqueda */}
        <div className="form-group">
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Cliente
          </label>
          <div className="dropdown-container relative">
            <input
              type="text"
              className={`w-full border ${errors.clienteId ? 'border-red-500' : 'border-gray-300'} rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500`}
              placeholder="Buscar cliente por nombre o identificación..."
              value={searchTerm}
              onChange={(e) => {
                setSearchTerm(e.target.value);
                setValue('clienteNombre', e.target.value);
                setShowDropdown(true);
              }}
              onFocus={() => setShowDropdown(true)}
            />
            <input type="hidden" {...register('clienteId', { required: 'Debe seleccionar un cliente' })} />
            <input type="hidden" {...register('clienteNombre')} />
            
            {showDropdown && filteredClientes.length > 0 && (
              <div className="absolute z-10 mt-1 w-full bg-white border border-gray-300 rounded-md shadow-lg max-h-60 overflow-auto">
                {filteredClientes.map(cliente => (
                  <div
                    key={cliente.id}
                    className="px-4 py-2 hover:bg-gray-100 cursor-pointer"
                    onClick={() => handleSelectCliente(cliente)}
                  >
                    <div className="font-medium">{cliente.nombre}</div>
                    <div className="text-sm text-gray-600">ID: {cliente.identificacion}</div>
                  </div>
                ))}
              </div>
            )}
            
            {searchTerm && showDropdown && filteredClientes.length === 0 && (
              <div className="absolute z-10 mt-1 w-full bg-white border border-gray-300 rounded-md shadow-lg p-4">
                <p className="text-gray-500">No se encontraron clientes con ese criterio</p>
              </div>
            )}
          </div>
          {errors.clienteId && (
            <p className="mt-1 text-sm text-red-600">{errors.clienteId.message}</p>
          )}
        </div>
        
        {/* Campo Saldo */}
        <div className="form-group">
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Saldo Inicial
          </label>
          <input
            type="number"
            step="0.01"
            min="0"
            className={`w-full border ${errors.saldo ? 'border-red-500' : 'border-gray-300'} rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500`}
            {...register('saldo', { 
              required: 'El saldo es obligatorio',
              min: {
                value: 0,
                message: 'El saldo no puede ser negativo'
              },
              valueAsNumber: true
            })}
          />
          {errors.saldo && (
            <p className="mt-1 text-sm text-red-600">{errors.saldo.message}</p>
          )}
        </div>
        
        {/* Botones */}
        <div className="flex justify-end space-x-3 pt-4">
          <button
            type="button"
            onClick={() => navigate('/cuentas')}
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
            {loading ? 'Guardando...' : 'Guardar'}
          </button>
        </div>
      </form>
    </div>
  );
};

export default CuentaForm;