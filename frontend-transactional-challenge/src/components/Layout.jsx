import { Link, Outlet } from 'react-router-dom';

const Layout = () => {
  return (
    <div className="min-h-screen bg-gray-100">
      {/* Header */}
      <header className="bg-white shadow-sm">
        <div className="max-w-7xl mx-auto px-4 py-4 sm:px-6 lg:px-8">
          <h1 className="text-2xl font-bold text-gray-800 text-center">
            BANCKING TRANSACTION CHALLENGE
          </h1>
        </div>
      </header>

      <div className="max-w-7xl mx-auto px-4 py-6 sm:px-6 lg:px-8">
        <div className="flex gap-6">
          {/* Sidebar Menu */}
          <aside className="w-64 bg-white rounded-lg shadow-md">
            <nav className="p-4">
              <h2 className="text-lg font-semibold mb-4 text-center">MENU</h2>
              <ul className="space-y-2">
                <li>
                  <Link
                    to="/clientes"
                    className="block w-full text-center py-2 px-4 rounded-md border border-gray-300 hover:bg-gray-100 transition-colors"
                  >
                    Clientes
                  </Link>
                </li>
                <li>
                  <Link
                    to="/cuentas"
                    className="block w-full text-center py-2 px-4 rounded-md border border-gray-300 hover:bg-gray-100 transition-colors"
                  >
                    Cuentas
                  </Link>
                </li>
                <li>
                  <Link
                    to="/movimientos"
                    className="block w-full text-center py-2 px-4 rounded-md border border-gray-300 hover:bg-gray-100 transition-colors"
                  >
                    Movimientos
                  </Link>
                </li>
              </ul>
            </nav>
          </aside>

          {/* Main Content */}
          <main className="flex-1 bg-white rounded-lg shadow-md p-6">
            <Outlet />
          </main>
        </div>
      </div>
    </div>
  );
};

export default Layout;