# API

## Сущности

1. Проект - Project
2. Задача - Task

## Описание сущности Project

1. Name (text)
2. Description (text)
3. CreatedBy (uuid)
4. CreatedAt (timestamp)

## Описание сущности Task

1. Type (enum)
2. Name (text)
3. Description (text)
4. Executor (uuid)
5. Priority (enum)
6. Status (enum)
7. Due date (timestamp)
8. CreatedBy (uuid)
9. CreatedAt (timestamp)

## Функции (эндпониты)

1. Project CRUDS
   1. create
   2. read
   3. update
   4. delete
   5. search

2. Task CRUDS
   1. create
   2. read
   3. update
   4. delete
   5. search
