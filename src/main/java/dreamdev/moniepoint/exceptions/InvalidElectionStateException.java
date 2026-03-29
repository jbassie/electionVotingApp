package dreamdev.moniepoint.exceptions;

public class InvalidElectionStateException extends VotingAppException {
    public InvalidElectionStateException(String message) {
        super(message);
    }
}
