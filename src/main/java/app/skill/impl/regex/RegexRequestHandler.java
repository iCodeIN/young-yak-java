package app.skill.impl.regex;

import app.handler.IHandlerInput;
import app.handler.IHandlerResponse;
import app.handler.IRequestHandler;
import app.handler.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;

public class RegexRequestHandler implements IRequestHandler {

    private static Random RANDOM = new Random(System.currentTimeMillis());
    private List<Pattern> patternList = new ArrayList<>();
    private List<String> replyList = new ArrayList<>();

    public RegexRequestHandler(Iterable<Pattern> patterns, Iterable<String> replies) {
        for (Pattern pattern : patterns)
            patternList.add(pattern);
        for (String reply : replies)
            replyList.add(reply);
    }

    @Override
    public boolean canHandle(IHandlerInput input) {
        for (Pattern pattern : patternList) {
            if (pattern.matcher(input.getContent().toString()).matches())
                return true;
        }
        return false;
    }

    @Override
    public Optional<IHandlerResponse> handle(IHandlerInput input) {
        String reply = replyList.get(RANDOM.nextInt(replyList.size()));
        IHandlerResponse response = new IHandlerResponse() {
            @Override
            public Status getStatus() {
                return Status.STATUS_200_OK;
            }

            @Override
            public Object getContent() {
                return reply;
            }
        };
        return Optional.ofNullable(response);
    }
}
