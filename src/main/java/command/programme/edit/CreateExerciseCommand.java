// @@author TVageesan

package command.programme.edit;

import command.CommandResult;
import programme.Day;
import programme.Exercise;
import programme.ProgrammeList;

import java.util.logging.Level;

/**
 * Command to create a new exercise within a specified day of a programme.
 * <p>
 * This class is responsible for creating an exercise and adding it to a specific day
 * within a specified programme.
 * </p>
 */
public class CreateExerciseCommand  extends EditCommand {
    public static final String SUCCESS_MESSAGE_FORMAT = "Created new exercise: %s%n";
    private final Exercise createdExercise;

    /**
     * Constructs a CreateExerciseCommand with the specified programme index, day ID, and exercise.
     *
     * @param programmeIndex the index of the programme to which the exercise will be added
     * @param dayId the ID of the day within the programme where the exercise will be inserted
     * @param createdExercise the exercise to be created and added to the day
     */
    public CreateExerciseCommand(int programmeIndex, int dayId, Exercise createdExercise) {
        super(programmeIndex,dayId);
        this.createdExercise = createdExercise;
        assert createdExercise != null : "created exercise must not be null";
    }

    /**
     * Executes the command to insert the created exercise into the specified day of the programme.
     * @param programmes the ProgrammeList that contains the programmes where the exercise will be added
     * @return a CommandResult containing a success message indicating the created exercise
     */
    public CommandResult execute(ProgrammeList programmes) {
        assert programmes != null : "programmes cannot be null";
        Day day = programmes.getDay(programmeIndex, dayIndex);
        day.insertExercise(createdExercise);
        String result = String.format(SUCCESS_MESSAGE_FORMAT, createdExercise);
        logger.log(Level.INFO, "CreateExerciseCommand executed successfully.");
        return new CommandResult(result);
    }
}
