# User Story 1: Создание проекта

## Сценарий 1: быстрое создание проекта

### TC-1: успешный

**Входные параметры:**

- **Наименование:** Тест
- **Описание:** Null

**Выходные параметры:**

- **Результат**: Успех
- **Объект:**
    - **Наименование:** Тест
    - **Описание:** Null

### TC-2: ошибка валидации заголовка

**Входные параметры:**

- **Наименование:** Null
- **Описание:** Null

**Выходные параметры:**

- **Результат**: Ошибка валидации: "наименование проекта не может быть пустым"
- **Объект:** Null

# User Story 2: Создание задачи

## Сценарий 1: быстрое создание задачи

### TC-1: успешный

**Входные параметры:**

- **Тип задачи:** Design
- **Наименование** Макет главной страницы
- **Описание:** Null
- **Исполнитель:** Null
- **Приоритет:** Средний

**Выходные параметры:**

- **Результат**: Успех
- **Объект:**
    - **Тип задачи:** Design
    - **Наименование** Макет главной страницы
    - **Описание:** Null
    - **Исполнитель:** Null
    - **Приоритет:** Средний
    - **Статус:** TODO

### TC-2: ошибка валидации наименования

**Входные параметры:**

- **Тип задачи:** Design
- **Наименование** Null
- **Описание:** Null
- **Исполнитель:** Null
- **Приоритет:** Средний

**Выходные параметры:**

- **Результат**: Ошибка валидации: "наименование задачи не может быть пустым"
- **Объект:** Null