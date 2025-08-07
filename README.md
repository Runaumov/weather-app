# Проект "Погода".

## Краткое описание
- Веб-приложение для запроса к внешнему API (openweathermap.org) и получения прогноза погоды (температуры, давления) по названию города.
- Архитектура: MVC.
- Имеется веб-интерфейс, написанный самостоятельно.
- В приложении реализована кастомная работа с куки и сессиями, без использования Spring Security.
- Техническое задание проекта находится [тут.]([https://zhukovsd.github.io/java-backend-learning-course/projects/currency-exchange/](https://zhukovsd.github.io/java-backend-learning-course/projects/weather-viewer/))

## Стек
- Spring MVC
- Hibernate
- PostgreSQL
- H2
- Maven
- Junit
- HikariCP
- log4j
- Flyway
- Bootstrap
- Thymeleaf
- Docker

## Допущения
- сделанный на коленке фронтенд (качественный фронтенд не является целью работы над приложением);
- конфигурация приложения реализована с помощью java-кода;
- не везде детально обрабатываются все возможные исключения;
- тесты минимально написаны исходя их технического задания проекта;


## Запуск приложения
- Получить API-ключ на openweathermap.org и положить его сюда: src/main/resources/application.properties с параметром weather.api.key
- С помощью Docker:
  - Соберите контейнер:
    ```bash
    docker build -t weather-app .
    ```
  - Запустите контейнер:
    ```bash
    docker run -p 8080:8080 weather-app
    ```
  - Адрес приложения: `http://localhost:8080/`
 - С помощью IDEA:
   - клонировать репозиторий;
   - поднять Docker-контейнер с PostgreSQL и запустить его: https://hub.docker.com/_/postgres
   - запустить в IDEA через Tomcat.
