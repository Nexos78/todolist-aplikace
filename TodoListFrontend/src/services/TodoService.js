const API_URL = import.meta.env.VITE_BACKEND_API_URL

async function apiCall(url, options = {}, errorPrefix) {
  const response = await fetch(url, options)

  if (!response.ok) {
    const errorText = await response.text()
    throw new Error(`${errorPrefix}: ${errorText}`)
  }

  if (options.method === 'DELETE') {
    return
  }

  return response.json()
}

export default {
  async getAllTodos() {
    return apiCall(API_URL, { cache: 'no-store' }, 'Chyba serveru při načtení úkolů')
  },

  async createTodo(newTodo) {
    return apiCall(
      API_URL,
      {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(newTodo),
      },
      'Chyba serveru při vytváření úkolu',
    )
  },

  async deleteTodo(id) {
    return apiCall(
      `${API_URL}/${id}`,
      {
        method: 'DELETE',
      },
      'Chyba serveru při mazání úkolu',
    )
  },

  async updateTodo(id, updatedTodo) {
    return apiCall(
      `${API_URL}/${id}`,
      {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(updatedTodo),
      },
      'Chyba serveru při úpravě úkolu',
    )
  },
}
