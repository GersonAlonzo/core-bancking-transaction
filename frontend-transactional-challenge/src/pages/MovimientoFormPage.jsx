import MovimientoForm from '../components/movimiento/MovimientoForm';

const MovimientoFormPage = () => {
  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">REGISTRO DE MOVIMIENTOS</h1>
      <div className="bg-white rounded-lg shadow-md p-6">
        <MovimientoForm />
      </div>
    </div>
  );
};

export default MovimientoFormPage;