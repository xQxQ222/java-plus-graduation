# Сервисы core:
* ## Comment-service (Работа с комментариями)
* ## Event-service (Работа с событиями, категориями, подборками)
* ## Request-service (Работа с заявками пользователей на участие в мероприятии)
* ## User-service (Сервис для администратора по управлению пользователями + методы для Feign клиента)

# Сервисы stats:
* ## Stats-server (Работа по сбору статистики обращений пользователей)
* ## Collector (Собирает данные по действиям пользователей с мероприятиями: просмотр, регистрация, лайк)
* ## Aggregator (Получает информацию о взаимодействии пользователей с мероприятиями и расчитывает для них сходство)
* ## Analyzer (Собирает данные из collector и aggregator и генерирует рейтинг для мероприятий и рекомендации для пользователей)

Таблица использованиями сервисов Feign клиентов:
| |Comment Feign|Event Feign|Request Feign|User Feign|Stats Feign|
|---------------|:-:|:-:|:-:|:-:|:-:|
|Comment-service| |+|-|+|-|
|Event-service|-| |+|+|+|
|Request-service|-|+| |+|-|
|User-service|-|-|-| |-|

Ссылки на спецификацию:
* ## [Main-service](https://github.com/xQxQ222/java-plus-graduation/blob/main/ewm-main-service-spec.json)
* ## [Stats-server](https://github.com/xQxQ222/java-plus-graduation/blob/main/ewm-stats-service-spec.json)

