# TODO List Aplikace

Tento projekt je implementací fullstack TODO list aplikace dle zadání. Skládá se z backendu v Javě (Spring Boot) a frontendové Single-Page Aplikace ve Vue.js.

## Použité technologie

- **Backend:** Java 17+, Spring Boot, Spring Data JPA, H2 Database, Validation, JUnit / Mockito
- **Frontend:** Vue 3 (Composition API), Vite, nativní Fetch API

---

## Architektonická rozhodnutí a obhajoba řešení

Při návrhu aplikace jsem se snažil najít ideální balanc mezi čistou architekturou (Separation of Concerns) a principy **KISS** (Keep It Simple, Stupid) a zamezením overengineeringu.

### Backend (Spring Boot)

- **Package by Feature:** Projekt nevyužívá zastaralé složkování podle vrstev (controllers, models), ale moderní přístup podle funkcionality (balíček `todo`). Všechny související třídy leží u sebe, což zajišťuje lepší zapouzdření a usnadňuje budoucí škálování (např. přechod na mikroslužby).
- **Zabezpečení a validace vstupů:** Do projektu je přidána závislost `spring-boot-starter-validation`. Entita je chráněna anotacemi (`@NotBlank`, `@Size`) a Controller využívá `@Valid`. Databáze je tak chráněna proti nekompletním datům hned na vstupu.
- **Absence komplexní Service vrstvy a DTO:** Vzhledem k tomu, že aplikace plní roli jednoduchého CRUD rozhraní nad jednou entitou, komunikuje Controller přímo s Repository. Zavádění DTO s MapStructem by zde představovalo zbytečnou duplikaci kódu.
- **In-memory databáze a CORS:** Pro lokální vývoj je využita H2 databáze (nevyžaduje externí konfiguraci) a CORS je vyřešen centrálně nad Controllerem pro bezproblémové spojení s Vue.

### Frontend (Vue.js)

- **Oddělení UI a API vrstvy:** Komunikace s backendem (HTTP požadavky) je vyčleněna do služby `TodoService.js`. Komponenty se tak starají výhradně o reaktivitu a renderování UI.
- **DRY princip v API voláních:** Opakující se logika pro `fetch` a zpracování chybových stavů (včetně vytažení chybové hlášky ze serveru) je v `TodoService.js` centralizována do jedné univerzální pomocné funkce.
- **Proměnné prostředí (.env):** URL adresa API není v kódu zadrátovaná napevno, ale načítá se dynamicky přes `import.meta.env`, což aplikaci připravuje pro nasazení na produkci.
- **Optimální UX a plynulost:** Aplikace využívá nativní Vue `<Transition>` pro plynulé animace. Při úpravě/mazání navíc nestahuje celou databázi znovu, ale upravuje reaktivní pole lokálně (optimistic UI updates), což zajišťuje okamžitou odezvu.

---

## Spuštění projektu

### 1. Spuštění Backendu

1. Otevřete složku backendu ve vašem IDE (IntelliJ IDEA / Eclipse).
2. Nechte stáhnout Maven/Gradle závislosti.
3. Spusťte hlavní třídu `TodolistBackendApplication`.
4. Backend poběží na adrese `http://localhost:8080`. Databáze se inicializuje automaticky.

### 2. Spuštění Frontendu

1. V terminálu/IDE přejděte do složky s frontendem.
2. Vytvořte soubor s lokální konfigurací adresy z připravené šablony:
   - Zkopírujte soubor `.env.example` a přejmenujte jej na `.env`.
3. Nainstalujte závislosti příkazem: `bash npm install`
4. Spusťte vývojový server příkazem: `npm run dev`
5. Frontend poběží na adrese `http://localhost:5173`
