import { Cookies } from 'react-cookie';


function backendFetch(path: String, options?: RequestInit) {
  const resource = path.startsWith('/') ? path.substring(1) : path;
  return fetch(`http://localhost:8080/${resource}`, {...options})
}

function authenticatedFetch(path: String, options?: RequestInit) {
  const resource = path.startsWith('/') ? path.substring(1) : path;
  return fetch(`http://localhost:8080/api/${resource}`, {
    ...options, 
    headers: {
      Authorization: `Bearer ${new Cookies().get("app-token")}`,
      ...options?.headers
    }
  })
}

export { backendFetch, authenticatedFetch }