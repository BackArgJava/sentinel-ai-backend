const baseURL = 'http://localhost:8081';

async function post(path, body) {
  const token = localStorage.getItem('sentinelToken') || sessionStorage.getItem('sentinelToken');
  const headers = { 'Content-Type': 'application/json' };
  if (token) headers['Authorization'] = `Bearer ${token}`;

  const res = await fetch(baseURL + path, {
    method: 'POST',
    headers,
    body: JSON.stringify(body)
  });

  if (res.status === 401 || res.status === 403) {
    localStorage.removeItem('sentinelToken');
    sessionStorage.removeItem('sentinelToken');
    const err = new Error('Token inválido/expirado');
    err.status = res.status;
    throw err;
  }

  if (!res.ok) {
    const text = await res.text();
    const err = new Error(text || 'Request failed');
    err.status = res.status;
    throw err;
  }

  const contentType = res.headers.get('content-type') || '';
  if (contentType.includes('application/json')) {
    return res.json();
  }
  return res.text();
}

export default { baseURL, post };
