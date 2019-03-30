package org.lightjason.agentspeak.action.bool;

import org.lightjason.agentspeak.common.IPath;

import javax.annotation.Nonnull;


/**
 * checks elements of equality.
 * The actions checks the first argument
 * to all others arguments of equality,
 * list structures won't be unflaten, but
 * elementwise compared.
 * On number arguments not the value must equal, also the type (double / integral) must be equal,
 * so keep in mind, that you use the correct number type on the argument input
 *
 * {@code [E1|E2] = .bool/equal( "this is equal", "this is equal", [123, "test"] );}
 */
public final class CEqual extends IBaseEqualiy
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -2953614515361905328L;
    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CEqual.class, "bool" );

    @Nonnull
    @Override
    public IPath name()
    {
        return NAME;
    }

    /**
     * apply to change boolean result
     *
     * @param p_value boolean result
     * @return boolean value
     */
    protected boolean apply( final boolean p_value )
    {
        return p_value;
    }

}
