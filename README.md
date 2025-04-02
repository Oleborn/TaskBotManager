# TaskBotManager

Телеграм-бот на Spring Boot для управления задачами с использованием PostgreSQL и возможностью деплоя через Docker.

## Описание проекта

TaskBotManager - это приложение для управления задачами через Telegram бота, разработанное на Spring Boot с использованием PostgreSQL в качестве базы данных. Проект демонстрирует создание полноценного приложения с возможностью деплоя через Docker и CI/CD с использованием Jenkins.

## Технологии

- Java 21
- Spring Boot 3.4.0
- Spring Data JPA
- PostgreSQL
- Telegram Bot API (telegrambots 6.9.7.1)
- MapStruct 1.5.5.Final
- Lombok
- Docker
- Jenkins (CI/CD)

## Архитектура проекта

Проект имеет модульную структуру и следует принципам чистой архитектуры:

### Основные компоненты

- **bot** - содержит классы для работы с Telegram Bot API
  - `Bot.java` - основной класс бота, наследующий TelegramLongPollingBot
  - `BotConfig.java` - конфигурация и регистрация бота

- **model** - содержит модели данных
  - **dto** - объекты передачи данных
  - **entities** - сущности базы данных
    - `Profile.java` - профиль пользователя
    - `Task.java` - задача

- **repository** - интерфейсы для работы с базой данных
  - `ProfileRepository.java` - репозиторий для работы с профилями
  - `TaskRepository.java` - репозиторий для работы с задачами

- **service** - бизнес-логика приложения

- **controller** - контроллеры для обработки запросов

- **mapper** - конвертеры между DTO и сущностями (с использованием MapStruct)

- **updatehandler** - обработчики сообщений от пользователей

- **scheduler** - планировщики для отправки уведомлений

- **utils** - вспомогательные утилиты

## Функциональность

- Создание и управление задачами через Telegram бота
- Добавление и удаление друзей
- Отправка уведомлений о задачах
- Учет часовых поясов пользователей
- Хранение данных в PostgreSQL

## Настройка и запуск

### Предварительные требования

- JDK 21
- Maven
- Docker и Docker Compose
- Telegram Bot Token (получается через [@BotFather](https://t.me/BotFather))

### Настройка переменных окружения

Для запуска приложения необходимо настроить следующие переменные окружения:
- `BOT_TOKEN` - токен Telegram бота
- `DB_PASSWORD` - пароль для базы данных PostgreSQL

### Запуск с использованием Docker Compose

1. Клонировать репозиторий:
   ```bash
   git clone https://github.com/Oleborn/TaskBotManager.git
   cd TaskBotManager
   ```

2. Настроить переменные окружения:
   ```bash
   export BOT_TOKEN=your_telegram_bot_token
   export DB_PASSWORD=your_database_password
   ```

3. Запустить приложение с помощью Docker Compose:
   ```bash
   docker-compose up -d
   ```

### Сборка и запуск без Docker

1. Клонировать репозиторий:
   ```bash
   git clone https://github.com/Oleborn/TaskBotManager.git
   cd TaskBotManager
   ```

2. Собрать проект с помощью Maven:
   ```bash
   ./mvnw clean package
   ```

3. Запустить приложение:
   ```bash
   java -jar target/TaskBot-0.0.1-SNAPSHOT.jar
   ```

## CI/CD с использованием Jenkins

Проект включает Jenkinsfile для настройки CI/CD пайплайна. Пайплайн выполняет следующие шаги:
- Сборка проекта
- Запуск тестов
- Создание Docker-образа
- Деплой приложения

## Использование

После запуска бота вы можете взаимодействовать с ним в Telegram:

1. Найдите своего бота по имени в Telegram
2. Отправьте команду `/start` для начала работы
3. Следуйте инструкциям бота для создания задач и управления ими

## Основные команды

- `/start` - начать взаимодействие с ботом
- `/help` - получить справку

## Особенности реализации

- Учет часовых поясов пользователей при создании задач
- Система уведомлений о предстоящих задачах
- Возможность добавления друзей и отправки им уведомлений
- Хранение данных в PostgreSQL с использованием Spring Data JPA
- Контейнеризация с помощью Docker

## Автор

- [Oleborn](https://github.com/Oleborn)
