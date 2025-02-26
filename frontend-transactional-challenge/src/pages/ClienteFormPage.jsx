import ClienteForm from '../components/cliente/ClienteForm';

const ClienteFormPage = () => {
  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">FORMULARIO DE CLIENTES</h1>
      <div className="card">
        <ClienteForm />
      </div>
    </div>
  );
};

export default ClienteFormPage;