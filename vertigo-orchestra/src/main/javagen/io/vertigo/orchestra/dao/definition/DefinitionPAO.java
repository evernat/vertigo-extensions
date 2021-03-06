package io.vertigo.orchestra.dao.definition;

import javax.inject.Inject;

import io.vertigo.app.Home;
import io.vertigo.dynamo.task.TaskManager;
import io.vertigo.dynamo.task.metamodel.TaskDefinition;
import io.vertigo.dynamo.task.model.Task;
import io.vertigo.dynamo.task.model.TaskBuilder;
import io.vertigo.dynamo.store.StoreServices;
import io.vertigo.lang.Assertion;
import io.vertigo.lang.Generated;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
 @Generated
public final class DefinitionPAO implements StoreServices {
	private final TaskManager taskManager;

	/**
	 * Constructeur.
	 * @param taskManager Manager des Task
	 */
	@Inject
	public DefinitionPAO(final TaskManager taskManager) {
		Assertion.checkNotNull(taskManager);
		//-----
		this.taskManager = taskManager;
	}

	/**
	 * Creates a taskBuilder.
	 * @param name  the name of the task
	 * @return the builder 
	 */
	private static TaskBuilder createTaskBuilder(final String name) {
		final TaskDefinition taskDefinition = Home.getApp().getDefinitionSpace().resolve(name, TaskDefinition.class);
		return Task.builder(taskDefinition);
	}

	/**
	 * Execute la tache TkDisableOldProcessDefinitions.
	 * @param name String 
	*/
	public void disableOldProcessDefinitions(final String name) {
		final Task task = createTaskBuilder("TkDisableOldProcessDefinitions")
				.addValue("name", name)
				.build();
		getTaskManager().execute(task);
	}

	/**
	 * Execute la tache TkGetProcessesByName.
	 * @param name String 
	 * @return Integer nombre
	*/
	public Integer getProcessesByName(final String name) {
		final Task task = createTaskBuilder("TkGetProcessesByName")
				.addValue("name", name)
				.build();
		return getTaskManager()
				.execute(task)
				.getResult();
	}

	private TaskManager getTaskManager() {
		return taskManager;
	}
}
