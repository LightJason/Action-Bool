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
 * counts the number of true values.
 * This actioncounts the number of true values
 *
 * {@code C = .bool/counttrue( Logical1, [Logical2, Logical3], Logical4 );}
 */
public final class CCountTrue extends IBaseAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 7257086360012435958L;
    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CCountTrue.class, "bool" );

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
        p_return.add(
            CRawTerm.of(
                CCommon.flatten( p_argument )
                       .map( ITerm::<Boolean>raw )
                       .filter( i -> i )
                       .count()
            )
        );

        return Stream.of();
    }

}
