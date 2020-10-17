# chat_bot

Бот для управления заданиями. Задание - это то, что Вы хотите сделать.

## Все возможности бота

0) Идентификация пользователей (логин + пароль или еще как-то)
1) Сами задания
2) Синхронизация с календарем
3) Напоминания
4) Напоминания с геолокацией
5) Совместные задания (Можно создавать группы, создавать общие задачи)
6) Статистика по времени, дням, выполненным заданиям

### Первая задача

1) Добавление задач
4) Просмотр всех задач
5) Удаление задач
7) Подключение к телеграму

### Вторая задача

1) Добавить возможность работы нескольких людей однвоременно
3) Сделать работу с телеграмом более интуитивно понятной (добавить кнопки, например)

### Третья задача

2) Добавить напоминания
  - Таймер, т.е. Через n минут бот напишет пользователю.

## Интерфейс

Для бота Telegram

/help

Выводит список команд с пояснением.

/add (time) [task]

- добавление задачи

task - Текстовое описание задачи.

time - Время для таймера в минутах

/del [task_id]

- Удаление задачи

task_id - Номер задачи, ее идентификатор (У каждой задачи есть id, который должен показыватся при выполнении команды /showt)

/deln [task_id]

- Удаление напоминания (Не входит в первую задачу)

task-id - Идентификатор задачи, у которой надо удалить таймер

/showt

- Просмотр задач

/shown

- Просмотр напоминаний (Не входит в первую задачу)

## Примеры

```
User: /addt Task1
Bot: Added Task1
U: /addt Task2
B: Added Task2
U: /showt
B: \ 0) Task1
1) Task2
U: /delt 0
B: Deleted Task1
```

```
User: /addt time=2 Task1
Bot: Added Task1
U: /showt
B: 0) Task1
U: /shown
B: 0) 2 min
```
