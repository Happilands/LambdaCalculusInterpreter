package interpreter.syntax.statements;

import interpreter.program.Program;
import interpreter.syntax.expressions.Expression;
import interpreter.syntax.expressions.Identifier;

public abstract class Statement {
    public abstract void run(Program program);

    public abstract void replace(Identifier identifier, Expression expression);
}
