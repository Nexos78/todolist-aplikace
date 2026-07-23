<script setup>
import { ref, onMounted } from 'vue'
import TodoService from '@/services/TodoService'

const todos = ref([])
const newTodo = ref('')
const editingId = ref(null)
const editingTitle = ref('')
const vFocus = { mounted: (el) => el.focus() }

onMounted(async () => {
  try {
    todos.value = await TodoService.getAllTodos()
  } catch (error) {
    console.error(error)
  }
})

const addTodo = async () => {
  if (newTodo.value.trim() !== '') {
    try {
      const createdTodo = await TodoService.createTodo({
        title: newTodo.value,
        completed: false,
      })
      todos.value.push(createdTodo)
      newTodo.value = ''
    } catch (error) {
      console.error(error)
    }
  }
}

const deleteTodo = async (id) => {
  try {
    await TodoService.deleteTodo(id)
    todos.value = todos.value.filter((todo) => todo.id !== id)
  } catch (error) {
    console.error(error)
  }
}

const toggleComplete = async (todo) => {
  try {
    const savedTodo = await TodoService.updateTodo(todo.id, {
      title: todo.title,
      completed: !todo.completed,
    })
    const index = todos.value.findIndex((t) => t.id === todo.id)
    todos.value[index] = savedTodo
  } catch (error) {
    console.error(error)
    todo.completed = !todo.completed
  }
}

const startEditing = (todo) => {
  editingId.value = todo.id
  editingTitle.value = todo.title
}

const cancelEdit = () => {
  editingId.value = null
}

const saveEdit = async (todo) => {
  if (editingTitle.value.trim() === '') {
    cancelEdit()
    return
  }

  try {
    const savedTodo = await TodoService.updateTodo(todo.id, {
      title: editingTitle.value,
      completed: todo.completed,
    })

    const index = todos.value.findIndex((t) => t.id === todo.id)
    todos.value[index] = savedTodo
    editingId.value = null
  } catch (error) {
    console.error(error)
  }
}
</script>
<template>
  <main>
    <h1 class="title">To-do List</h1>

    <form @submit.prevent="addTodo" class="add-form">
      <input type="text" v-model="newTodo" placeholder="Co je potřeba udělat?" />
      <button type="submit" class="add-btn">Přidat úkol</button>
    </form>

    <ul class="todo-list">
      <li v-for="todo in todos" :key="todo.id" class="todo-item">
        <label class="todo-label">
          <input
            type="checkbox"
            :checked="todo.completed"
            @change="toggleComplete(todo)"
            class="checkbox"
          />
        </label>

        <Transition name="fade" mode="out-in">
          <div v-if="editingId === todo.id" class="edit-mode">
            <input
              type="text"
              v-model="editingTitle"
              @keyup.enter="saveEdit(todo)"
              @keyup.esc="cancelEdit"
              v-focus
              class="edit-input"
            />
            <button @click="saveEdit(todo)" class="save-btn">Uložit</button>
            <button @click="cancelEdit" class="cancel-btn">Zrušit</button>
          </div>

          <div v-else class="view-mode">
            <span :class="{ 'is-completed': todo.completed }" class="todo-text">
              {{ todo.title }}
            </span>
            <button @click="startEditing(todo)" class="edit-btn">Upravit</button>
            <button @click="deleteTodo(todo.id)" class="delete-btn">Smazat</button>
          </div>
        </Transition>
      </li>
    </ul>
  </main>
</template>
<style scoped>
main {
  max-width: 600px;
  margin: 2rem auto;
  font-family: Arial, sans-serif;
  padding: 0 1rem;
}

.title {
  text-align: center;
}

.add-form {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 2rem;
}

.add-form input {
  flex-grow: 1;
  padding: 0.5rem;
  outline: none;
  border: 2px solid;
  border-radius: 15px;
}

.todo-list {
  list-style-type: none;
  padding: 0;
}

.todo-item {
  display: flex;
  align-items: center;
  padding: 0.75rem;
  border-bottom: 1px solid hsl(0, 0%, 93%);
  gap: 1rem;
}

.todo-label {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.todo-text {
  flex-grow: 1;
}

.is-completed {
  text-decoration: line-through;
  color: hsl(0, 0%, 53%);
}

.checkbox {
  appearance: none;
  width: 24px;
  height: 24px;
  border: 2px solid hsl(0, 0%, 80%);
  border-radius: 5px;
  background-color: hsl(0, 0%, 100%);
  cursor: pointer;
  transition: all 0.2s ease-in-out;
}

.checkbox:hover {
  border-color: hsl(100, 74%, 53%);
  background-color: hsl(90, 100%, 96%);
}

.checkbox:checked {
  background-color: hsl(100, 74%, 53%);
  border-color: hsl(100, 74%, 53%);
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='none' stroke='white' stroke-width='4' stroke-linecap='round' stroke-linejoin='round'%3E%3Cpolyline points='20 6 9 17 4 12'%3E%3C/polyline%3E%3C/svg%3E");
  background-size: 70%;
  background-position: center;
  background-repeat: no-repeat;
}

.view-mode,
.edit-mode {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  flex-grow: 1;
}

.edit-input {
  flex-grow: 1;
  padding: 0.3rem;
  outline: none;
  border: 2px solid;
  border-radius: 10px;
}

button {
  border: none;
  padding: 0.4rem 0.8rem;
  border-radius: 5px;
  cursor: pointer;
}

.add-btn {
  background-color: hsl(0, 0%, 0%);
  color: hsl(0, 0%, 93%);
}

.edit-btn {
  background-color: hsl(209, 100%, 50%);
  color: hsl(0, 0%, 100%);
}
.save-btn {
  background-color: hsl(100, 74%, 53%);
  color: hsl(0, 0%, 100%);
}
.cancel-btn {
  background-color: hsl(0, 6%, 83%);
  color: hsl(0, 0%, 0%);
}
.delete-btn {
  background-color: hsl(359, 100%, 50%);
  color: hsl(0, 0%, 100%);
}

button:hover {
  opacity: 0.7;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.1s ease-in-out;
}

/* 2. Z jakého a do jakého stavu se má prvek animovat (nulová průhlednost) */
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

@media (max-width: 500px) {
  .add-form {
    flex-direction: column;
  }

  .add-form input,
  .add-btn {
    width: 100%;
    box-sizing: border-box;
  }

  .view-mode,
  .edit-mode {
    flex-wrap: wrap;
  }

  .todo-text,
  .edit-input {
    flex-basis: 100%;
    margin-bottom: 0.5rem;
  }

  .view-mode button,
  .edit-mode button {
    flex-grow: 1;
    padding: 0.6rem;
  }
}
</style>
