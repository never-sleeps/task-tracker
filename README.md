### Task Tracker

Заведение задач, отслеживание выполнения
 ____________________________________________________

#### Project structure

| Module name               | Description                                          |
|---------------------------|------------------------------------------------------|
| **acceptance**            | Модуль с приёмочными тестами (1)                     |
| **common**                | Модуль с внутренними моделями проекта                |
| **api-jackson**           | Модуль с jackson-serialization/deserialization       |
| **api-multiplatform**     | Модуль с multiplatform-serialization/deserialization |
| **mappers-jackson**       | Маппинг транспортных и внутренних моделей (2)        |
| **mappers-multiplatform** | Маппинг транспортных и внутренних моделей (2)        |
| **app-spring**            | Модуль на Spring Framework (3)                       |
| **business**              | Модуль с бизнес-логикой                              |
| **stubs**                 | Модуль с заглушками                                  |




(1) Использует модели из `api-multiplatform`  
(2) Маппинги эквивалентны, различаются лишь импортами используемых моделей 
(для `mappers-jackson` импортируются модели из `api-jackson`, для `mappers-multiplatform` – модели из `api-multiplatform`)  
(3) Использует модели из `api-jackson`  