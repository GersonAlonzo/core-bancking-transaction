import { useEffect, useState, useCallback, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import { AgGridReact } from 'ag-grid-react';
import 'ag-grid-community/styles/ag-grid.css';
import 'ag-grid-community/styles/ag-theme-alpine.css';
// Importar el módulo necesario
import { ClientSideRowModelModule } from 'ag-grid-community';
import { clienteService } from '../../services/apiServices';

const ClienteTable = () => {
  const [clientes, setClientes] = useState([]);
  const [loading, setLoading] = useState(true);
  const gridRef = useRef(null);
  const navigate = useNavigate();



  const getRowStyle = () => {
    return { 
      height: '60px', 
      alignItems: 'center'
    };
  };
  const columnDefs = [
    { field: 'id', headerName: 'ID', flex: 1, filter: false, hide:true },
    { field: 'nombre', headerName: 'Nombre', flex: 1, filter: true },
    { field: 'identificacion', headerName: 'Identificación', flex: 1, filter: true },
    { field: 'tipoIdentificacion', headerName: 'Tipo ID', flex: 1, filter: true },
    { field: 'fechaNacimiento', headerName: 'Fecha Nacimiento', flex: 1, filter: true },
    {
      headerName: 'Acciones',
      flex: 1,
      cellRenderer: (params) => {
        return (
          <div className="flex space-x-2">
            <button
              onClick={() => handleEdit(params.data.id)}
              className="px-3 py-1 bg-blue-500 text-white rounded hover:bg-blue-600"
            >
              Editar
            </button>
            <button
              onClick={() => handleDelete(params.data.id)}
              className="px-3 py-1 bg-red-500 text-white rounded hover:bg-red-600"
            >
              Eliminar
            </button>
          </div>
        );
      }
    }
  ];

  // Cargar datos
  const loadClientes = useCallback(async () => {
    try {
      setLoading(true);
      const response = await clienteService.getAll();
      setClientes(response.data);
    } catch (error) {
      console.error('Error al cargar clientes:', error);
      // Manejar el error - mostrar mensaje al usuario
      setClientes([]);
    } finally {
      setLoading(false);
    }
  }, []);

  // Editar cliente
  const handleEdit = (id) => {
    navigate(`/clientes/editar/${id}`);
  };

  // Eliminar cliente
  const handleDelete = async (id) => {
    if (window.confirm('¿Está seguro de eliminar este cliente?')) {
      try {
        await clienteService.delete(id);
        // Refrescar la tabla
        loadClientes();
      } catch (error) {
        console.error('Error al eliminar cliente:', error);
        alert(`Error: ${error.response?.data?.mensaje || 'No se pudo eliminar el cliente'}`);
      }
    }
  };

  // Cargar datos al montar el componente
  useEffect(() => {
    loadClientes();
  }, [loadClientes]);

  return (
    <div className="w-full h-full">
      <div className="mb-4 flex justify-between items-center">
        <h2 className="text-xl font-semibold">Registros</h2>
        <button
          onClick={() => navigate('/clientes/nuevo')}
          className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
        >
          Agregar nuevo registro
        </button>
      </div>
      
      <div className="ag-theme-alpine w-full h-[600px]">
        <AgGridReact
          ref={gridRef}
          rowData={clientes}
          columnDefs={columnDefs}
          pagination={true}
          paginationPageSize={10}
          rowSelection="single"
          suppressCellFocus={true}
          enableCellTextSelection={true}
          // Registrar los módulos necesarios
          modules={[ClientSideRowModelModule]}
          defaultColDef={{
            sortable: true,
            resizable: true
          }}
          rowHeight={40}
        />
      </div>
    </div>
  );
};

export default ClienteTable;