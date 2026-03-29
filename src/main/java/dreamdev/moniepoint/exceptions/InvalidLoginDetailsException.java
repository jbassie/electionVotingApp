package dreamdev.moniepoint.exceptions;

import dreamdev.moniepoint.dtos.request.VotersLoginRequest;

public class InvalidLoginDetailsException extends VotingAppException {
    public InvalidLoginDetailsException(String message) {
        super(message);
    }
}
