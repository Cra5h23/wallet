# Приложение Wallet

## Описание проекта

Данное приложение реализует REST API для управления кошельками с поддержкой операций пополнения и снятия средств.
Приложение разработано на Java с использованием Spring Framework и PostgreSQL, а также включает в себя миграции базы
данных с помощью Liquibase. Проект обеспечивает высокую производительность и устойчивость к конкурентным запросам.

## Стек технологий

- Java 17
- Spring Framework 3.3.4
- PostgreSQL
- Liquibase
- Docker
- Docker Compose

## Эндпоинты

### 1. Создание кошелька

**POST** `/api/v1/new-wallet`

**Ответ**
-**201**: Кошелёк успешно создан.

- **500 Internal Server Error**: Ошибка сервера.

### 2. Обновление кошелька

**POST** `/api/v1/wallet`

**Тело запроса:**

```json
{
  "walletId": "UUID",
  "operationType": "DEPOSIT" | "WITHDRAW",
  "amount": 1000
}
```

**Ответ:**

- **200 OK**: Успешное выполнение операции.
- **400 Bad Request**: Неверный JSON.
- **404 Not Found**: Запрашиваемый кошелёк не существует.
- **402 Payment Required**: Недостаточно средств на счету.
- **500 Internal Server Error**: Ошибка сервера.

### 3. Получение баланса кошелька

**GET** `/api/v1/wallets/{WALLET_UUID}`

**Ответ:**

- **200 OK**: Возвращает текущий баланс кошелька.
- **404 Not Found**: Кошелек не существует.

## Конкурентная обработка

Приложение оптимизировано для обработки до 1000 запросов в секунду (RPS) на один кошелек. Все запросы обрабатываются
асинхронно, что обеспечивает высокую производительность и минимизацию ошибок.

## Настройка

### Docker

Для запуска приложения и базы данных в Docker-контейнерах, используйте `docker-compose`.

### Параметры конфигурации

Параметры приложения и базы данных могут быть настроены через переменные окружения в файле `.env` без необходимости
пересборки контейнеров.

## Миграции

Миграции для базы данных осуществляются с помощью Liquibase. Все изменения в структуре базы данных должны быть прописаны
в соответствующих файлах миграции.

## Тестирование

Все эндпоинты покрыты тестами, что обеспечивает надежность и корректность работы приложения.

## Запуск приложения

1. Склонируйте репозиторий:
   ```bash
   git clone https://github.com/Cra5h23/wallet.git
   cd wallet
   ```

2. Создайте файл `.env` в корне проекта и настройте параметры подключения к базе данных.

3. Запустите Docker Compose:
   ```bash
   docker-compose up --build
   ```

4. Приложение будет доступно по адресу `http://localhost:8080`.

## Ссылка на репозиторий

[Ссылка на репозиторий GitHub](https://github.com/Cra5h23/wallet)

## Цель создания приложения

Приложение создано в качестве тестого задания для компании Java Code


<details>
<summary>Текст задания:</summary>
   
         Добрый день, уважаемый соискатель, данное задание нацелено на выявление вашего
      реального уровня в разработке на java, поэтому отнеситесь к нему, как к работе на проекте. 
      Выполняйте его честно и проявите себя по максимуму, удачи!
      Напишите приложение, которое по REST принимает запрос вида
      POST api/v1/wallet
            {
               "walletId": "UUID",
               "operationType": "DEPOSIT" | "WITHDRAW",
               "amount": 1000
            }  
      
      после выполнять логику по изменению счета в базе данных
      также есть возможность получить баланс кошелька
      GET api/v1/wallets/{WALLET_UUID}
      
      стек:
      java 8-17
      Spring 3
      Postgresql
      
         Должны быть написаны миграции для базы данных с помощью liquibase
      Обратите особое внимание проблемам при работе в конкурентной среде 
      (1000 RPS по одному кошельку). 
      Ни один запрос не должен быть не обработан (50Х error)
      Предусмотрите соблюдение формата ответа для заведомо неверных запросов, когда
      кошелька не существует, не валидный json, или недостаточно средств.
      приложение должно запускаться в докер контейнере, база данных тоже, вся система
      должна подниматься с помощью docker-compose
      предусмотрите возможность настраивать различные параметры как на стороне
      приложения так и базы данных без пересборки контейнеров.
      эндпоинты должны быть покрыты тестами.
         Решенное задание залить на гитхаб, предоставить ссылку
      Все возникающие вопросы по заданию решать самостоятельно, по своему
      усмотрению.
</details>
