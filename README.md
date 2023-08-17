### Task Tracker

Заведение задач, отслеживание выполнения
 ____________________________________________________

#### Project structure

| Module name            | Description                                     |
|------------------------|-------------------------------------------------|
| _acceptance_           | Модуль с приёмочными тестами (1)                |
| common                 | Модуль с внутренними моделями проекта           |
| api-...                | Модули с serialization/deserialization          |
| mappers-...            | Маппинг транспортных и внутренних моделей       |
| **app-...**            | Модули с использованием определённых технологий |
| **app-repository-...** | Модули с для работы с БД                        |
| lib-cor                | Chain of Responsibility Design Pattern Library  |
| lib-logging-...        | Модули для логирования                          |
| **business**           | Модуль с бизнес-логикой                         |
| stubs                  | Модуль с заглушками                             |






- `mappers-jackson` импортируются модели из `api-jackson`, для `mappers-multiplatform` – модели из `api-multiplatform`
- `v1` = `jackson`, `v2` = `multiplatform`
