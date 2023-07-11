### Task Tracker

Заведение задач, отслеживание выполнения
 ____________________________________________________

#### Project structure

| Module name           | Description                                          |
|-----------------------|------------------------------------------------------|
| _acceptance_          | Модуль с приёмочными тестами (1)                     |
| common                | Модуль с внутренними моделями проекта                |
| api-jackson           | Модуль с jackson-serialization/deserialization       |
| api-multiplatform     | Модуль с multiplatform-serialization/deserialization |
| mappers-jackson       | Маппинг транспортных и внутренних моделей (2)        |
| mappers-multiplatform | Маппинг транспортных и внутренних моделей (2)        |
| **app-spring**        | Модуль на Spring Framework (3)                       |
| **app-ktor**          | Модуль на Ktor Framework (4)                         |
| **app-serverless**    | serverless модуль                                    |
| **app-rabbitmq**      | RabbitMQ module                                      |
| **app-kafka**         | Kafka module                                         |
| **business**          | Модуль с бизнес-логикой                              |
| stubs                 | Модуль с заглушками                                  |




(1) Использует модели из `api-multiplatform`  
(2) Маппинги эквивалентны, различаются лишь импортами используемых моделей 
(для `mappers-jackson` импортируются модели из `api-jackson`, для `mappers-multiplatform` – модели из `api-multiplatform`)  
(3) Использует модели из `api-jackson`  
(4) Использует модели из `api-jackson` в `jvmMain` и модели из `api-multiplatform` в `commonMain`. `jvmTest` содержит тесты для обоих типов api (и для `jackson`, и для `multiplatform`). Для них внутри модуля появляется разделение на `v1` и `v2` 
