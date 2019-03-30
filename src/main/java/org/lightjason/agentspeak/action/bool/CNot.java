package org.lightjason.agentspeak.action.bool;

import org.lightjason.agentspeak.action.IBaseAction;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Stream;


/**
 * inverts all argument.
 * This action uses the logical negation and
 * inverts all logical boolean arguments and returns
 * all elements
 *
 * {@code [R1|R2|R3|R4] = .bool/not( Logical1, [Logical2, Logical3], Logical4 );}
 *
 * @see https://en.wikipedia.org/wiki/Negation
 */
public final class CNot extends IBaseAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -7990703518931629152L;
    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CNot.class, "bool" );

    @Nonnull
    @Override
    public IPath name()
    {
        return NAME;
    }

    @Nonnegative
    @Override
    public int minimalArgumentNumber()
    {
        return 1;
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                           @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        CCommon.flatten( p_argument )
               .map( ITerm::<Boolean>raw )
               .map( i -> !i )
               .map( CRawTerm::of )
               .forEach( p_return::add );

        return Stream.of();
    }

}
