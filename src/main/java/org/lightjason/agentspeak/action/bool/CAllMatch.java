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
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * checks all elements are equal to the first argument.
 * The actions checks the first argument to all other if this
 * matchs for equality.
 * On number arguments not the value must equal, also the type (double / integral) must be equal,
 * so keep in mind, that you use the correct number type on the argument input
 *
 * {@code AllEqual = .bool/allmatch( "this is the test", 123, "this is the test", ["hello", 234] );}
 */
public final class CAllMatch extends IBaseAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -5787849046607404490L;
    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CAllMatch.class, "bool" );

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
        final List<?> l_arguments = CCommon.flatten( p_argument ).map( ITerm::raw ).collect( Collectors.toList() );

        p_return.add(
            CRawTerm.of(
                l_arguments.stream()
                           .skip( 1 )
                           .allMatch( i -> l_arguments.get( 0 ).equals( i ) )
            )
        );

        return Stream.of();
    }

}
