import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react(), tailwindcss()],
  server: {
    port: 5176,
    host: '0.0.0.0',
    proxy: {
      '/api': {
        target: 'http://backend-rest:8081',
        changeOrigin: true,
        secure: false,
        rewrite: (path) => path
      }
    }
  }
})
