/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason                                                #
 * # Copyright (c) 2015-19, LightJason (info@lightjason.org)                            #
 * # This program is free software: you can redistribute it and/or modify               #
 * # it under the terms of the GNU Lesser General Public License as                     #
 * # published by the Free Software Foundation, either version 3 of the                 #
 * # License, or (at your option) any later version.                                    #
 * #                                                                                    #
 * # This program is distributed in the hope that it will be useful,                    #
 * # but WITHOUT ANY WARRANTY; without even the implied warranty of                     #
 * # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      #
 * # GNU Lesser General Public License for more details.                                #
 * #                                                                                    #
 * # You should have received a copy of the GNU Lesser General Public License           #
 * # along with this program. If not, see http://www.gnu.org/licenses/                  #
 * ######################################################################################
 * @endcond
 */

package org.lightjason.agentspeak.action.bool;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.lightjason.agentspeak.action.IAction;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.testing.IBaseTest;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * test for boolean actions
 */
public final class TestCActionBool extends IBaseTest
{

    /**
     * data provider generator
     * @return data
     */
    public static Stream<Arguments> generate()
    {
        return Stream.of(
            Arguments.of( Stream.of( true, false, true, false, false, true ), CAllMatch.class, Stream.of( false ) ),
            Arguments.of( Stream.of( true, false, true, false, false, true ), CAnyMatch.class, Stream.of( true ) ),
            Arguments.of( Stream.of( true, false, true, false, false, true ), CAnd.class, Stream.of( false ) ),
            Arguments.of( Stream.of( true, false, true, false, false, true ), COr.class, Stream.of( true ) ),
            Arguments.of( Stream.of( true, false, true, false, false, true ), CXor.class, Stream.of( true ) ),
            Arguments.of( Stream.of( true, false, true, false, false, true ), CNot.class, Stream.of( false, true, false, true, true, false ) ),
            Arguments.of( Stream.of( true, false, true, false, false, true ), CCountTrue.class, Stream.of( 3L ) ),
            Arguments.of( Stream.of( true, false, true, false, false, true ), CCountFalse.class, Stream.of( 3L ) ),

            Arguments.of( Stream.of( true, true ), CAllMatch.class, Stream.of( true ) ),
            Arguments.of( Stream.of( true, true ), CAnyMatch.class, Stream.of( true ) ),
            Arguments.of( Stream.of( true, true ), CAnd.class, Stream.of( true ) ),
            Arguments.of( Stream.of( true, true ), COr.class, Stream.of( true ) ),
            Arguments.of( Stream.of( true, true ), CXor.class, Stream.of( false ) ),
            Arguments.of( Stream.of( true, true ), CNot.class, Stream.of( false, false ) ),
            Arguments.of( Stream.of( true, true ), CCountTrue.class, Stream.of( 2L ) ),
            Arguments.of( Stream.of( true, true ), CCountFalse.class, Stream.of( 0L ) )
        );
    }

    /**
     * generic test-case
     *
     * @param p_input test-case data
     * @param p_actionclass action class
     * @param p_result expected result
     * @throws IllegalAccessException is thrwon on instantiation error
     * @throws InstantiationException is thrwon on instantiation error
     * @throws NoSuchMethodException is thrwon on instantiation error
     * @throws InvocationTargetException is thrown on instantiation error
     */
    @ParameterizedTest
    @MethodSource( "generate" )
    public void execute( @Nonnull final Stream<Object> p_input, @Nonnull final Class<IAction> p_actionclass, final Stream<Object> p_result )
        throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException
    {
        final List<ITerm> l_return = new ArrayList<>();

        p_actionclass.getConstructor().newInstance().execute(
            false, IContext.EMPTYPLAN,
            p_input.map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assertions.assertArrayEquals(
            l_return.stream().map( ITerm::raw ).toArray(),
            p_result.toArray()
        );
    }

    /**
     * test equal
     */
    @Test
    public void equal()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CEqual().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_return, l_return, l_return, new Object() ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assertions.assertEquals( 2, l_return.size() );
        Assertions.assertTrue( l_return.get( 0 ).<Boolean>raw() );
        Assertions.assertFalse( l_return.get( 1 ).<Boolean>raw() );


        final List<Integer> l_list1 = IntStream.range( 0, 5 ).boxed().collect( Collectors.toList() );
        final List<Integer> l_list2 = IntStream.range( 0, 5 ).boxed().collect( Collectors.toList() );

        new CEqual().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_list1, l_list2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assertions.assertEquals( 3, l_return.size() );
        Assertions.assertTrue( l_return.get( 2 ).<Boolean>raw() );


        final Map<Integer, Integer> l_map1 = new HashMap<>();
        l_map1.put( 1, 2 );
        final Map<Integer, Integer> l_map2 = new HashMap<>();
        l_map2.put( 1, 1 );

        new CEqual().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_map1, l_map2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assertions.assertEquals( 4, l_return.size() );
        Assertions.assertFalse( l_return.get( 3 ).<Boolean>raw() );
    }

    /**
     * test not-equal
     */
    @Test
    public void notequal()
    {
        final Object l_object = new Object();
        final List<ITerm> l_return = new ArrayList<>();

        new CNotEqual().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_object, l_object, l_object, new Object() ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assertions.assertEquals( 2, l_return.size() );
        Assertions.assertFalse( l_return.get( 0 ).<Boolean>raw() );
        Assertions.assertTrue( l_return.get( 1 ).<Boolean>raw() );
    }

}
