import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App'
import './index.css'
import { UserProvider } from "./context/UserContext";
import { Provider } from 'react-redux';
import { store } from './redux/store';



ReactDOM.createRoot(document.getElementById('root')).render(
	<React.StrictMode>
		<Provider store={store}>
			<UserProvider>
				<App />
			</UserProvider>
		</Provider>
	</React.StrictMode>
)
