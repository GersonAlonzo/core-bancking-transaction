import { useEffect, useState, useCallback, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import { AgGridReact } from 'ag-grid-react';
import 'ag-grid-community/styles/ag-grid.css';
import 'ag-grid-community/styles/ag-theme-balham.css'; // Cambiamos a un tema más minimalista
import { ClientSideRowModelModule } from 'ag-grid-community';
import { movimientoService } from '../../services/apiServices';

const MovimientoTable = () => {
  const [movimientos, setMovimientos] = useState([]);
  const [loading, setLoading] = useState(true);
  const gridRef = useRef(null);
  const navigate = useNavigate();

  const columnDefs = [
    { 
      field: 'numeroReferencia', 
      headerName: 'Referencia', 
      flex: 1.2, 
      filter: true,
      tooltipField: 'numeroReferencia'
    },
    { 
      field: 'tipoMovimiento', 
      headerName: 'Tipo', 
      flex: 1.2, 
      filter: true,
      cellRenderer: (params) => {
        const tipo = params.value;
        
        let baseClasses = "px-2 py-1 rounded-full text-xs font-medium ";
        
        if (tipo?.includes('DEPOSITO')) {
          return <span className={baseClasses + "bg-green-100 text-green-800"}>{tipo}</span>;
        } else if (tipo?.includes('RETIRO')) {
          return <span className={baseClasses + "bg-orange-100 text-orange-800"}>{tipo}</span>;
        } else {
          return <span className={baseClasses + "bg-blue-100 text-blue-800"}>{tipo}</span>;
        }
      }
    },
    { field: 'cuentaOrigen', headerName: 'Cuenta Origen', flex: 1.2, filter: true },
    { field: 'cuentaDestino', headerName: 'Cuenta Destino', flex: 1.2, filter: true },
    { 
      field: 'monto', 
      headerName: 'Monto', 
      flex: 1, 
      filter: true,
      valueFormatter: (params) => {
        return params.value ? `L${params.value.toFixed(2)}` : 'L0.00';
      },
      cellStyle: (params) => {
        if (params.data?.tipoMovimiento?.includes('Depósito')) {
          return { color: '#047857', fontWeight: '500' }; 
        } else if (params.data?.tipoMovimiento?.includes('Retiro')) {
          return { color: '#C2410C', fontWeight: '500' }; 
        }
        return {};
      }
    },
    { 
      field: 'fechaMovimiento', 
      headerName: 'Fecha', 
      flex: 1.2,
      filter: true,
      valueFormatter: (params) => {
        if (!params.value) return '';
        const date = new Date(params.value);
        return date.toLocaleString();
      }
    }
  ];

  const loadAllMovimientos = useCallback(async () => {
    try {
      setLoading(true);
      const response = await movimientoService.getAll();
      setMovimientos(response.data);
    } catch (error) {
      console.error('Error al cargar movimientos:', error);
      setMovimientos([]);
    } finally {
      setLoading(false);
    }
  }, []);


  const handleNuevoMovimiento = () => {
    navigate('/movimientos/nuevo');
  };

  useEffect(() => {
    loadAllMovimientos();
  }, [loadAllMovimientos]);


  useEffect(() => {
    const handleResize = () => {
      if (gridRef.current && gridRef.current.api) {
        gridRef.current.api.sizeColumnsToFit();
      }
    };

    window.addEventListener('resize', handleResize);
    return () => window.removeEventListener('resize', handleResize);
  }, []);

  return (
    <div className="w-full h-full">
      <div className="mb-6">
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-xl font-semibold text-gray-800">Registros</h2>
          <button
            onClick={handleNuevoMovimiento}
            className="bg-blue-500 hover:bg-blue-600 text-white font-medium py-2 px-4 rounded-md shadow-sm transition-colors focus:outline-none focus:ring-2 focus:ring-blue-300"
          >
            Registrar Movimiento
          </button>
        </div>
      </div>
      
      <div className="w-full rounded-lg shadow-md overflow-hidden">
        <div className="ag-theme-balham w-full h-[600px]" style={{ fontSize: '14px' }}>
          <AgGridReact
            ref={gridRef}
            rowData={movimientos}
            columnDefs={columnDefs}
            pagination={true}
            paginationPageSize={10}
            rowSelection="single"
            suppressCellFocus={true}
            modules={[ClientSideRowModelModule]}
            enableCellTextSelection={true}
            defaultColDef={{
              sortable: true,
              filter: true,
              resizable: true
            }}
            animateRows={true}
            rowHeight={50}
            headerHeight={48}
            onGridReady={(params) => {
              params.api.sizeColumnsToFit();
            }}
            overlayLoadingTemplate={
              '<div class="flex justify-center items-center h-full"><div class="text-gray-500 animate-pulse">Cargando datos...</div></div>'
            }
            overlayNoRowsTemplate={
              '<div class="flex justify-center items-center h-full"><div class="text-gray-500">No hay datos disponibles</div></div>'
            }
          />
        </div>
      </div>
    </div>
  );
};

export default MovimientoTable;