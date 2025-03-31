import react from '@vitejs/plugin-react'
import legacy from '@vitejs/plugin-legacy'
import path from 'path'
require('dotenv').config()

const getConfig = ({ command, mode }) => ({
    plugins: [react(), legacy()],
	server: {
		port: process.env.PORT || 3000,
	  },
	resolve:{
		alias:{
		  '~' : path.resolve(__dirname, './src')
		},
	},
	vite: {
		esbuild: {
		  loader: {
			'.js': 'jsx',
		  }
		}
	}
})

export default getConfig
