import CuentaForm from '../components/cuenta/CuentaForm';

const CuentaFormPage = () => {
  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">FORMULARIO DE CUENTA</h1>
      <div className="card">
        <CuentaForm />
      </div>
    </div>
  );
};

export default CuentaFormPage;