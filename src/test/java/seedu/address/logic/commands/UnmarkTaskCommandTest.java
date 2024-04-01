package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND;
import static seedu.address.testutil.TypicalTasks.getTypicalTaskList;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.task.Task;
import seedu.address.model.TaskList;
import seedu.address.testutil.TaskBuilder;

public class UnmarkTaskCommandTest {
    private Model model = new ModelManager(new AddressBook(), getTypicalTaskList(), new UserPrefs());

    @Test
    public void execute_validIndexTaskList_success() {
        Task taskToMark = model.getTaskList().getSerializeTaskList().get(INDEX_FIRST.getZeroBased());
        UnmarkTaskCommand unmarkTaskCommand = new UnmarkTaskCommand(INDEX_FIRST);

        String expectedMessage = String.format(UnmarkTaskCommand.MESSAGE_UNMARK_TASK_SUCCESS,
                Messages.formatTask(taskToMark));

        ModelManager expectedModel = new ModelManager(new AddressBook(), model.getTaskList(), new UserPrefs());
        taskToMark.getStatus().setAsUndone();

        assertCommandSuccess(unmarkTaskCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexTaskList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getTaskList().getSerializeTaskList().size() + 1);
        UnmarkTaskCommand unmarkTaskCommand = new UnmarkTaskCommand(outOfBoundIndex);

        assertCommandFailure(unmarkTaskCommand, model, Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    @Test
    public void execute_taskWithoutDeadline() {
        Task taskWithoutDeadline = new TaskBuilder().withTaskName("Task 1").withTaskDeadline("Empty").build();
        model.addTask(taskWithoutDeadline);
        Index noDeadlineTask = Index.fromOneBased(model.getTaskList().getSerializeTaskList().size());
        UnmarkTaskCommand unmarkTaskCommand = new UnmarkTaskCommand(noDeadlineTask);

        String expectedMessage = String.format(UnmarkTaskCommand.MESSAGE_UNMARK_TASK_SUCCESS,
                Messages.formatTask(taskWithoutDeadline));

        ModelManager expectedModel = new ModelManager(new AddressBook(), model.getTaskList(), new UserPrefs());

        assertCommandSuccess(unmarkTaskCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_taskWithDeadline() {
        TaskList taskList = new TaskList();
        Model m = new ModelManager(new AddressBook(), taskList, new UserPrefs());
        Task taskWithDeadline = new TaskBuilder()
                .withTaskName("Task 1")
                .withTaskDeadline("12-12-2024 16:00")
                .build();
        m.addTask(taskWithDeadline);
        Index deadlineTask = Index.fromOneBased(m.getTaskList().getSerializeTaskList().size());
        UnmarkTaskCommand unmarkTaskCommand = new UnmarkTaskCommand(deadlineTask);

        String expectedMessage = String.format(UnmarkTaskCommand.MESSAGE_UNMARK_TASK_SUCCESS,
                Messages.formatTask(taskWithDeadline));

        ModelManager expectedModel = new ModelManager(new AddressBook(), m.getTaskList(), new UserPrefs());

        assertCommandSuccess(unmarkTaskCommand, m, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        UnmarkTaskCommand unmarkTaskFirstCommand = new UnmarkTaskCommand(INDEX_FIRST);
        UnmarkTaskCommand unmarkTaskSecondCommand = new UnmarkTaskCommand(INDEX_SECOND);

        // same object -> returns true
        assertTrue(unmarkTaskFirstCommand.equals(unmarkTaskFirstCommand));

        // same values -> returns true
        UnmarkTaskCommand unmarkTaskFirstCommandCopy = new UnmarkTaskCommand(INDEX_FIRST);
        assertTrue(unmarkTaskFirstCommand.equals(unmarkTaskFirstCommandCopy));

        // different types -> returns false
        assertFalse(unmarkTaskFirstCommand.equals(1));

        // null -> returns false
        assertFalse(unmarkTaskFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(unmarkTaskFirstCommand.equals(unmarkTaskSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        UnmarkTaskCommand unmarkTaskCommand = new UnmarkTaskCommand(targetIndex);
        String expected = UnmarkTaskCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, unmarkTaskCommand.toString());
    }
}
