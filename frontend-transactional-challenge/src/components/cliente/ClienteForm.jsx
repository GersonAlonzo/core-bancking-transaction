import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { clienteService } from '../../services/apiServices';

const ClienteForm = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [loading, setLoading] = useState(false);
  const [apiError, setApiError] = useState('');
  const isEditMode = Boolean(id);

  // Configuración de React Hook Form
  const {
    register,
    handleSubmit,
    formState: { errors },
    setValue
  } = useForm({
    defaultValues: {
      nombre: '',
      identificacion: '',
      tipoIdentificacion: 'CI', 
      fechaNacimiento: ''
    }
  });

  useEffect(() => {
    const loadCliente = async () => {
      if (isEditMode) {
        try {
          setLoading(true);
          setApiError(''); 
          const response = await clienteService.getById(id);
          const cliente = response.data;
          const fechaNacimiento = cliente.fechaNacimiento 
            ? cliente.fechaNacimiento.split('T')[0] 
            : '';
          setValue('nombre', cliente.nombre);
          setValue('identificacion', cliente.identificacion);
          setValue('tipoIdentificacion', cliente.tipoIdentificacion);
          setValue('fechaNacimiento', fechaNacimiento);
        } catch (error) {
          console.error('Error al cargar el cliente:', error);
          setApiError('No se pudo cargar la información del cliente. Por favor, intente más tarde.');
          navigate('/clientes');
        } finally {
          setLoading(false);
        }
      }
    };

    loadCliente();
  }, [id, isEditMode, setValue, navigate]);

  const onSubmit = async (data) => {
    try {
      setLoading(true);
      setApiError(''); 
      if (isEditMode) {
        await clienteService.update({
          ...data,
          id
        });
      } else {
        await clienteService.create(data);
      }

      navigate('/clientes');
    } catch (error) {
      console.error('Error al guardar cliente:', error);
      const errorMessage = error.response?.data?.mensaje || 'No se pudo guardar el cliente';
      setApiError(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="w-full">
      <h2 className="text-xl font-semibold mb-6">
        {isEditMode ? 'Editar Cliente' : 'Registrar Nuevo Cliente'}
      </h2>
      
      {/* Mensaje de error de la API */}
      {apiError && (
        <div className="mb-4 p-3 bg-red-50 border border-red-300 rounded-md">
          <p className="text-red-600 font-medium">{apiError}</p>
        </div>
      )}
      
      <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
        {/* Campo Nombre */}
        <div className="form-group">
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Nombre
          </label>
          <input
            type="text"
            className={`w-full border ${errors.nombre ? 'border-red-500' : 'border-gray-300'} rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500`}
            {...register('nombre', { 
              required: 'El nombre es obligatorio',
              maxLength: {
                value: 100,
                message: 'El nombre no debe exceder 100 caracteres'
              }
            })}
          />
          {errors.nombre && (
            <p className="mt-1 text-sm text-red-600">{errors.nombre.message}</p>
          )}
        </div>
        {/* Campo Tipo de Identificación */}
        <div className="form-group">
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Tipo de Identificación
          </label>
          <select
            className={`w-full border ${errors.tipoIdentificacion ? 'border-red-500' : 'border-gray-300'} rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500`}
            {...register('tipoIdentificacion', { 
              required: 'El tipo de identificación es obligatorio'
            })}
          >
            <option value="CI">Cédula de Identidad (CI)</option>
            <option value="PA">Pasaporte (PA)</option>
          </select>
          {errors.tipoIdentificacion && (
            <p className="mt-1 text-sm text-red-600">{errors.tipoIdentificacion.message}</p>
          )}
        </div>
        
        {/* Campo Identificación */}
        <div className="form-group">
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Identificación
          </label>
          <input
            type="text"
            className={`w-full border ${errors.identificacion ? 'border-red-500' : 'border-gray-300'} rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500`}
            {...register('identificacion', { 
              required: 'La identificación es obligatoria'
            })}
          />
          {errors.identificacion && (
            <p className="mt-1 text-sm text-red-600">{errors.identificacion.message}</p>
          )}
        </div>
        
        
        {/* Campo Fecha de Nacimiento */}
        <div className="form-group">
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Fecha de Nacimiento
          </label>
          <input
            type="date"
            className={`w-full border ${errors.fechaNacimiento ? 'border-red-500' : 'border-gray-300'} rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500`}
            {...register('fechaNacimiento', { 
              required: 'La fecha de nacimiento es obligatoria'
            })}
          />
          {errors.fechaNacimiento && (
            <p className="mt-1 text-sm text-red-600">{errors.fechaNacimiento.message}</p>
          )}
        </div>
        
        {/* Botones */}
        <div className="flex justify-end space-x-3 pt-4">
          <button
            type="button"
            onClick={() => navigate('/clientes')}
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

export default ClienteForm;