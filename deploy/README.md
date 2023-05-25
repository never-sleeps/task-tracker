# deploy

## Настройка
Для корректного запуска файлов добавить в настройки ядра Linux параметр:

**/etc/sysctl.d/20-opensearch.conf:**
```
vm.max_map_count = 262144
```
