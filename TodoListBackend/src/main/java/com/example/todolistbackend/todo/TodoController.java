package com.example.todolistbackend.todo;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@Tag(name = "Úkoly", description = "API pro správu TODO listu")
public class TodoController {

    private final TodoRepository todoRepository;

    public TodoController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @GetMapping
    @Operation(summary = "Získá všechny úkoly", description = "Vrátí kompletní seznam úkolů seřazený podle ID.")
    public List<TodoEntry> getAllTodos() {
        return todoRepository.findAll();
    }

    @PostMapping
    @Operation(summary = "Vytvoří nový úkol", description = "Uloží nový úkol do databáze a vrátí ho s přiděleným ID.")
    @ApiResponse(responseCode = "200", description = "Úkol byl úspěšně vytvořen")
    @ApiResponse(responseCode = "400", description = "Neplatná vstupní data (např. prázdný název)")
    public TodoEntry createTodo(@Valid @RequestBody TodoEntry newTodo) {
        return todoRepository.save(newTodo);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Smaže úkol", description = "Trvale odstraní úkol z databáze podle jeho ID.")
    @ApiResponse(responseCode = "204", description = "Úkol byl úspěšně smazán (nevrací žádná data)")
    @ApiResponse(responseCode = "404", description = "Úkol s tímto ID nebyl nalezen")
    public void deleteTodo(@PathVariable Long id) {
        todoRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Upraví existující úkol", description = "Přepíše data existujícího úkolu podle zadaného ID a vrátí upravený záznam.")
    @ApiResponse(responseCode = "200", description = "Úkol byl úspěšně upraven")
    @ApiResponse(responseCode = "400", description = "Neplatná vstupní data (např. chybějící povinná pole)")
    @ApiResponse(responseCode = "404", description = "Úkol s tímto ID nebyl nalezen")
    public TodoEntry updateTodo(@PathVariable Long id, @Valid @RequestBody TodoEntry updatedTodo) {
        TodoEntry existingTodo = todoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Úkol s tímto id neexistuje."));

        existingTodo.setTitle(updatedTodo.getTitle());
        existingTodo.setCompleted(updatedTodo.isCompleted());
        return todoRepository.save(existingTodo);
    }
}
