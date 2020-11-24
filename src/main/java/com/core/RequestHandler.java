package com.core;

import com.fsm.FSM;
import com.fsm.State;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.function.BiConsumer;

/**
 * Класс, который является прослойкой между ConsoleBot или Bot и Core
 *
 * @author Lev
 */
public class RequestHandler {
    private Core core = new Core();
    private FSM fsm = new FSM();

    /**
     * Проверка на то, что тело команды задано корректно
     * @param body Тело команды
     * @return True, если все хорошо
     */
    private boolean bodyIsCorrect(String body) {
        return body != null && !StringUtils.isBlank(body) && !body.equals("");
    }

    public State getUserFSMState(String uid) { return core.getUserFSMState(uid); }

    public State getFSMState(){
        return fsm.getCurrentState();
    }

    private void updateFSMState(String uid){
        State userState = core.getUserFSMState(uid);
        if (userState == null){
            core.createUser(uid);
            fsm.setState(State.ENTRY_POINT);
        } else{
            fsm.setState(userState);
        }
    }

    /**
     * Обработка сообщения
     * @param input - сообщение
     * @return Строка с резульатом, которую надо показать пользователю
     */
    public String handle(String uid, String chatId, String input, BiConsumer<String, String> func) throws ParseException {
        updateFSMState(uid);

        if (input.equals("/fsmstate"))
            return fsm.getCurrentState().toString();

        String res = null;
        boolean isAdd = fsm.isState(State.ADD);
        boolean isDel = fsm.isState(State.DEL);
        boolean isDone = fsm.isState(State.DONE);
        boolean isClear = fsm.isState(State.CLEAR);
        boolean isShowCompleted = fsm.isState(State.SHOW_COMPLETED);
        boolean isShowTodo = fsm.isState(State.SHOW_TODO);
        boolean isHelp = fsm.isState(State.HELP);
        boolean isStart = fsm.isState(State.START);
        boolean isCancel = input.equals(Constants.CANCEL_COMMAND);
        boolean isNotification = fsm.isState(State.NOTIFICATION);

        if (isShowCompleted || isShowTodo || isHelp || isStart || isClear)
            fsm.update();

        fsm.update(input);

        if(isAdd && !input.equals(Constants.CANCEL_COMMAND)){
            res = addTask(uid, input);
        }
        else if (isDel && !input.equals(Constants.CANCEL_COMMAND)){
            res = deleteTask(uid, input, false);
        }
        else if (isDone && !input.equals(Constants.CANCEL_COMMAND)){
            res = deleteTask(uid, input, true);
        }
        else if(isNotification && !input.equals(Constants.CANCEL_COMMAND)){
            var converter = new DateConverter();
            var date = converter.parse(getTime(input));
            if(date != null){
                res = setTimer(uid, chatId, Integer.parseInt(getTaskId(input)), date, func);
            }
            else {
                res = "None";
            }
        }
        else if (fsm.isState(State.SHOW_COMPLETED)){
            res = core.getCompletedTasks(uid);
        }
        else if (fsm.isState(State.SHOW_TODO)){
            res = core.getToDoTasks(uid);
        }
        else if (fsm.isState(State.CLEAR)){
            res = clearTaskList(uid);
        }
        else if(fsm.isState(State.LISTEN)){
            if (!isCancel)
                res = Constants.INCORRECT_COMMAND_MESSAGE;
            else
                res = Constants.BOT_WAITING_COMMANDS;
        }
        else {
            res = fsm.getCurrentState().getStateMessage();
        }

        core.setUserFSMState(uid, fsm.getCurrentState());

        return res;
    }

    private String getTaskId(String message){
        int position = message.indexOf(" ");

        return message.substring(0, position);
    }

    private String getTime(String message){
        int position = message.indexOf(" ");

        return message.substring(position + 1);
    }

    private String setTimer(String uid, String chatId, int taskid, Date date, BiConsumer<String, String> func){
        core.setTimer(uid, chatId, taskid, date, func);

        return "Added";
    }

    /**
     * Обертка над методом addTask класса Core.
     *
     * @param taskDescription Текст задачи
     * @return Сообщение об результате операции
     */
    private String addTask(String uid, String taskDescription){
        String result;

        if (bodyIsCorrect(taskDescription)) {
            core.addTask(uid, taskDescription);
            result = Constants.TASK_ADDED_MSG + taskDescription;
        } else {
            result = Constants.EMPTY_TASK_DESCRIPTION_MSG;
        }

        return result;
    }

    /**
     * Обертка над методом deleteTask класса Core
     *
     * @param taskId Принимается идентификатор задачи
     * @return Сообщение об результате операции
     */
    private String deleteTask(String uid, String taskId, Boolean markAsCompleted){
        String result;

        if (bodyIsCorrect(taskId)) {
            try {
                core.deleteTask(uid, taskId, markAsCompleted);
                if (markAsCompleted){
                    result = Constants.TASK_COMPLETED_MSG + taskId;
                } else{
                    result = Constants.TASK_DELETED_MSG + taskId;
                }
            } catch (NotExistingTaskIndexException | IncorrectTaskIdTypeException exception) {
                result = exception.getMessage();
            }
        } else {
            result = Constants.EMPTY_TASK_ID_MSG;
        }

        return result;
    }

    /**
     * Обертка над методом emptyTaskList класса Core
     *
     * @param uid Id пользователя, у которого нужно очистить список задач
     * @return Сообщение об результате операции
     */
    private String clearTaskList(String uid){
        core.clearTaskList(uid);

        return Constants.CLEARED_TASK_LIST_MSG;
    }
}

