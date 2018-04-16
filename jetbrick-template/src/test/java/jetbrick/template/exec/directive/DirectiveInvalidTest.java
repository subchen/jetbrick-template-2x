package jetbrick.template.exec.directive;

import jetbrick.template.Errors;
import jetbrick.template.exec.AbstractJetxTest;
import jetbrick.template.parser.SyntaxException;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

public class DirectiveInvalidTest extends AbstractJetxTest {

    @Test
    public void testInvalid_define() {
        thrown.expect(SyntaxException.class);
        thrown.expectMessage(CoreMatchers.containsString(err(Errors.ARGUMENTS_MISSING)));

        eval("#define");
    }

    @Test
    public void testInvalid_if() {
        thrown.expect(SyntaxException.class);
        thrown.expectMessage(CoreMatchers.containsString(err(Errors.ARGUMENTS_MISSING)));

        eval("#if");
    }

    @Test
    public void testInvalid_missing_end() {
        thrown.expect(SyntaxException.class);
        thrown.expectMessage(CoreMatchers.containsString("mismatched input '<EOF>' expecting {DIRECTIVE_OPEN_ELSEIF, DIRECTIVE_ELSE, DIRECTIVE_END}"));

        eval("#if(true)123");
    }

    @Test
    public void testInvalid_break() {
        thrown.expect(SyntaxException.class);
        thrown.expectMessage(CoreMatchers.containsString("cannot be used outside of"));

        eval("#break");
    }

    @Test
    public void testInvalid_continue() {
        thrown.expect(SyntaxException.class);
        thrown.expectMessage(CoreMatchers.containsString("cannot be used outside of"));

        eval("#continue");
    }

}
