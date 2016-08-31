package com.sun.codemodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class JNarrowedInvocation extends JExpressionImpl implements JStatement {

    /**
     * Object expression upon which this method will be invoked, or null if
     * this is a constructor invocation
     */
    private JGenerable object;

    /**
     * Name of the method to be invoked.
     * Either this field is set, or {@link #method}, or {@link #type} (in which case it's a
     * constructor invocation.)
     * This allows {@link JMethod#name(String) the name of the method to be changed later}.
     */
    private String name;

    private JMethod method;

    /**
     * List of argument expressions for this method invocation
     */
    private List<JExpression> args = new ArrayList<JExpression>();

    private final List<JClass> narrow;
    

    /**
     * Invokes a method on an object.
     *
     * @param object
     *        JExpression for the object upon which
     *        the named method will be invoked,
     *        or null if none
     *
     * @param name
     *        Name of method to invoke
     */
    public JNarrowedInvocation(JExpression object, String name, JClass... narrow) {
        this( (JGenerable)object, name, narrow);
    }

    public JNarrowedInvocation(JExpression object, JMethod method, JClass... narrow) {
        this( (JGenerable)object, method, narrow );
    }
    
    /**
     * Invokes a static method on a class.
     */
    public JNarrowedInvocation(JClass type, String name, JClass... narrow) {
        this( (JGenerable)type, name, narrow );
    }

    public JNarrowedInvocation(JClass type, JMethod method, JClass... narrow) {
        this( (JGenerable)type, method, narrow );
    }

    private JNarrowedInvocation(JGenerable object, String name, JClass... narrow) {
        this.object = object;
        if (name.indexOf('.') >= 0)
            throw new IllegalArgumentException("method name contains '.': " + name);
        this.name = name;
        this.narrow = Arrays.asList(narrow);
    }

    private JNarrowedInvocation(JGenerable object, JMethod method, JClass... narrow) {
        this.object = object;
        this.method =method;
        this.narrow = Arrays.asList(narrow);
    }

    /**
     *  Add an expression to this invocation's argument list
     *
     * @param arg
     *        Argument to add to argument list
     */
    public JNarrowedInvocation arg(JExpression arg) {
        if(arg==null)   throw new IllegalArgumentException();
        args.add(arg);
        return this;
    }

    /**
     * Adds a literal argument.
     *
     * Short for {@code arg(JExpr.lit(v))}
     */
    public JNarrowedInvocation arg(String v) {
        return arg(JExpr.lit(v));
    }
    
	/**
	 * Returns all arguments of the invocation.
	 * @return
	 *      If there's no arguments, an empty array will be returned.
	 */
	public JExpression[] listArgs() {
		return args.toArray(new JExpression[args.size()]);
	}

    public void generate(JFormatter f) {
        String name = this.name;
        if(name==null)  name=this.method.name();

        f.g(object).p('.').p('<').g(narrow).p(JFormatter.CLOSE_TYPE_ARGS).p(name).p('(');
                
        f.g(args);

        f.p(')');
    }

    public void state(JFormatter f) {
        f.g(this).p(';').nl();
    }
    
}