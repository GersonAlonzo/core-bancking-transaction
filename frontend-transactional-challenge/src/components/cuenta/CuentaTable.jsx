import { useEffect, useState, useCallback, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import { AgGridReact } from 'ag-grid-react';
import 'ag-grid-community/styles/ag-grid.css';
import 'ag-grid-community/styles/ag-theme-alpine.css';
import { ClientSideRowModelModule } from 'ag-grid-community';
import { cuentaService, clienteService } from '../../services/apiServices';

const CuentaTable = () => {
  const [cuentas, setCuentas] = useState([]);
  const [clientesMap, setClientesMap] = useState({});
  const [loading, setLoading] = useState(true);
  const gridRef = useRef(null);
  const navigate = useNavigate();

  const columnDefs = [
    { field: 'numeroCuenta', headerName: 'Número de Cuenta', flex: 1, filter: true,cellClass: 'selectable-cell' },
    { 
      field: 'clienteId', 
      headerName: 'Cliente', 
      flex: 1, 
      filter: true,
      valueFormatter: (params) => {
        return clientesMap[params.value] || params.value;
      }
    },
    { field: 'fechaApertura', headerName: 'Fecha Apertura', flex: 1, filter: true },
    { field: 'estadoCuenta', headerName: 'Estado', flex: 1, filter: true },
    { 
      field: 'saldo', 
      headerName: 'Saldo', 
      flex: 1, 
      filter: true,
      valueFormatter: (params) => {
        return params.value ? `L${params.value.toFixed(2)}` : '$0.00';
      }
    },
    {
      headerName: 'Acciones',
      flex: 1,
      cellRenderer: (params) => {
        return (
          <div className="flex space-x-2">
            <button
              onClick={() => handleDelete(params.data.numeroCuenta)}
              className="px-3 py-1 bg-red-500 text-white rounded hover:bg-red-600"
            >
              Eliminar
            </button>
          </div>
        );
      }
    }
  ];

  const loadClientes = useCallback(async () => {
    try {
      const response = await clienteService.getAll();
      const map = {};
      response.data.forEach(cliente => {
        map[cliente.id] = cliente.nombre;
      });
      setClientesMap(map);
    } catch (error) {
      console.error('Error al cargar clientes:', error);
      setClientesMap({});
    }
  }, []);

  // Cargar cuentas
  const loadCuentas = useCallback(async () => {
    try {
      setLoading(true);
      const response = await cuentaService.getAll();
      setCuentas(response.data);
    } catch (error) {
      console.error('Error al cargar cuentas:', error);
      setCuentas([]);
    } finally {
      setLoading(false);
    }
  }, []);

  const handleDelete = async (numeroCuenta) => {
    if (window.confirm('¿Está seguro de eliminar esta cuenta?')) {
      try {
        await cuentaService.delete(numeroCuenta);
        loadCuentas();
      } catch (error) {
        console.error('Error al eliminar cuenta:', error);
        alert(`Error: ${error.response?.data?.mensaje || 'No se pudo eliminar la cuenta'}`);
      }
    }
  };

  useEffect(() => {
    loadClientes();
    loadCuentas();
  }, [loadClientes, loadCuentas]);

  return (
    <div className="w-full h-full">
      <div className="mb-4 flex justify-between items-center">
        <h2 className="text-xl font-semibold">Registros</h2>
        <button
          onClick={() => navigate('/cuentas/nuevo')}
          className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
        >
          Agregar nueva cuenta
        </button>
      </div>
      
      <div className="ag-theme-alpine w-full h-[500px]">
        <AgGridReact
          ref={gridRef}
          rowData={cuentas}
          columnDefs={columnDefs}
          pagination={true}
          paginationPageSize={10}
          enableCellTextSelection={true}
          rowSelection="single"
          suppressCellFocus={true}
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

export default CuentaTable;