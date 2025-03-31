import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import legacy from '@vitejs/plugin-legacy'
import path from 'path'
require('dotenv').config()


export default defineConfig({
  plugins: [react(), legacy()],
  define: {
    'process.env': process.env
  },
  server: {
    port: process.env.PORT || 3000,
  },
  resolve: {
    alias: {
      '~' : path.resolve(__dirname, './src')
    },
  },
  vite: {
		esbuild: {
		  loader: {
			'.js': 'jsx',
		  }
		}
	},
})
