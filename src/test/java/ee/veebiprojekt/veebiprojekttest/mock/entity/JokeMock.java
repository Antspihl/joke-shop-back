package ee.veebiprojekt.veebiprojekttest.mock.entity;

import ee.veebiprojekt.veebiprojekttest.entity.Joke;

public class JokeMock {

    public static Joke shallowJoke(Long id){
        return Joke.builder().
                id(id).
                setup("Mock Joke").
                punchline("Mock Joke Punchline").
                build();
    }
}
