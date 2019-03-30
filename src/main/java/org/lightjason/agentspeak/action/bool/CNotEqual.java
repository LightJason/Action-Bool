package org.lightjason.agentspeak.action.bool;


import org.lightjason.agentspeak.common.IPath;

import javax.annotation.Nonnull;


/**
 * checks elements of inequality.
 * The actions checks the first argument
 * to all others arguments of unequality,
 * list structures won't be unflaten, but
 * elementwise compared.
 * On number arguments not the value must equal, also
 * the type (double / integral) must be equal, so keep
 * in mind, that you use the correct number type on the argument input
 *
 * {@code [NE1|NE2] = .bool/notequal( "this is equal", "this is equal", [123, "test"] );}
 */
public final class CNotEqual extends IBaseEqualiy
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -3226848482113693419L;
    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CNotEqual.class, "bool" );

    @Nonnull
    @Override
    public IPath name()
    {
        return NAME;
    }

    @Override
    protected boolean apply( final boolean p_value )
    {
        return !p_value;
    }

}
