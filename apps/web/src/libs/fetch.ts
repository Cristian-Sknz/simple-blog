import { Cookies } from 'react-cookie';
import { ErrorResponse } from '@remix-run/router';


function backendFetch(path: string, options?: RequestInit) {
  const resource = path.startsWith('/') ? path.substring(1) : path;
  return fetch(`http://localhost:8080/${resource}`, {...options})
}

function authenticatedFetch(path: string, options?: RequestInit) {
  const resource = path.startsWith('/') ? path.substring(1) : path;
  return fetch(`http://localhost:8080/api/${resource}`, {
    ...options, 
    headers: {
      'Authorization': `Bearer ${new Cookies().get("app-token")}`,
      'Content-Type': 'application/json',
      ...options?.headers
    }
  })
}

async function fetcher<T = any>(path: string) {
  const response = await authenticatedFetch(path)
  if (response.status >= 200 
      && response.status < 400) {
    return (await response.json()) as T
  }

  if (response.status === 401) {
    throw new ErrorResponse(response.status, response.statusText, "Usuário não esta autenticado.");
  }

  const json = await response.json() as Record<string, any>
  if (!json?.message) {
    throw new ErrorResponse(response.status, response.statusText, "Ocorreu um erro ao fazer uma requisição")
  }

  throw new ErrorResponse(response.status, response.statusText, json.message);
}

async function poster<T = any>(path: string, args?: any) {
  const response = await authenticatedFetch(path, {
    method: 'POST',
    body: JSON.stringify(args?.arg),
  });

  if (response.status >= 200 
      && response.status < 400) {
    return response.json() as T
  }
  if (response.status === 401) {
    throw new ErrorResponse(response.status, response.statusText, "Usuário não esta autenticado.");
  }

  const json = await response.json() as Record<string, any>
  if (!json?.message) {
    throw new ErrorResponse(response.status, response.statusText, "Ocorreu um erro ao fazer uma requisição")
  }

  throw new ErrorResponse(response.status, response.statusText, json.message);
}

type Messages = {
  [status: number]: string | undefined
}

function handleError(error: ErrorResponse | null, messages: Messages): string | null {
  return error && new ErrorResponse(
      error?.status || 500, 
      error?.statusText || 'Ocorreu um erro', 
      messages[error?.status] || error?.data
    ).data
}


export { backendFetch, authenticatedFetch, fetcher, poster, handleError }