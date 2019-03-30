package org.lightjason.agentspeak.action.bool;

import com.codepoetics.protonpack.StreamUtils;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lightjason.agentspeak.action.IAction;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.testing.IBaseTest;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * test for boolean actions
 */
@RunWith( DataProviderRunner.class )
public final class TestCActionBool extends IBaseTest
{

    /**
     * data provider generator
     * @return data
     */
    @DataProvider
    public static Object[] generate()
    {
        return Stream.concat(

            // -- first test-case ---
            testcase(

                // input data
                Stream.of( true, false, true, false, false, true ),

                // testing classes / test-methods
                Stream.of(
                    CAllMatch.class,
                    CAnyMatch.class,
                    CAnd.class,
                    COr.class,
                    CXor.class,
                    CNot.class,
                    CCountTrue.class,
                    CCountFalse.class
                ),

                // results for each class test
                Stream.of( false ),
                Stream.of( true ),
                Stream.of( false ),
                Stream.of( true ),
                Stream.of( true ),
                Stream.of( false, true, false, true, true, false ),
                Stream.of( 3L ),
                Stream.of( 3L )
            ),



            // --- second test-case ---
            testcase(

                // input data
                Stream.of( true, true ),

                // testing classes / test-methods
                Stream.of(
                    CAllMatch.class,
                    CAnyMatch.class,
                    CAnd.class,
                    COr.class,
                    CXor.class,
                    CNot.class,
                    CCountTrue.class,
                    CCountFalse.class

                ),

                // results for each class test
                Stream.of( true ),
                Stream.of( true ),
                Stream.of( true ),
                Stream.of( true ),
                Stream.of( false ),
                Stream.of( false, false ),
                Stream.of( 2L ),
                Stream.of( 0L )
            )

        ).toArray();
    }

    /**
     * method to generate test-cases
     *
     * @param p_input input data
     * @param p_classes matching test-classes / test-cases
     * @param p_classresult result for each class
     * @return test-object
     */
    @SafeVarargs
    @SuppressWarnings( "varargs" )
    private static Stream<Object> testcase( final Stream<Object> p_input, final Stream<Class<?>> p_classes, final Stream<Object>... p_classresult )
    {
        final List<ITerm> l_input = p_input.map( CRawTerm::of ).collect( Collectors.toList() );

        return StreamUtils.zip(
            p_classes,
            Arrays.stream( p_classresult ),
            ( i, j ) -> new ImmutableTriple<>( l_input, i, j )
        );
    }

    /**
     * generic test-case
     *
     * @param p_input test-case data
     * @throws IllegalAccessException is thrwon on instantiation error
     * @throws InstantiationException is thrwon on instantiation error
     * @throws NoSuchMethodException is thrwon on instantiation error
     * @throws InvocationTargetException is thrwon on instantiation error
     */
    @Test
    @UseDataProvider( "generate" )
    public void execute( final Triple<List<ITerm>, Class<? extends IAction>, Stream<Object>> p_input )
        throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException
    {
        final List<ITerm> l_return = new ArrayList<>();

        p_input.getMiddle().getConstructor().newInstance().execute(
            false, IContext.EMPTYPLAN,
            p_input.getLeft(),
            l_return
        );

        Assert.assertArrayEquals(
            l_return.stream().map( ITerm::raw ).toArray(),
            p_input.getRight().toArray()
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
            Stream.of( l_return, l_return, new Object() ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertTrue( l_return.get( 0 ).<Boolean>raw() );
        Assert.assertFalse( l_return.get( 1 ).<Boolean>raw() );


        final List<Integer> l_list1 = IntStream.range( 0, 5 ).boxed().collect( Collectors.toList() );
        final List<Integer> l_list2 = IntStream.range( 0, 5 ).boxed().collect( Collectors.toList() );

        new CEqual().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_list1, l_list2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 3, l_return.size() );
        Assert.assertTrue( l_return.get( 2 ).<Boolean>raw() );


        final Map<Integer, Integer> l_map1 = new HashMap<>();
        l_map1.put( 1, 2 );
        final Map<Integer, Integer> l_map2 = new HashMap<>();
        l_map2.put( 1, 1 );

        new CEqual().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_map1, l_map2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 4, l_return.size() );
        Assert.assertFalse( l_return.get( 3 ).<Boolean>raw() );
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
            Stream.of( l_object, l_object, new Object() ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertFalse( l_return.get( 0 ).<Boolean>raw() );
        Assert.assertTrue( l_return.get( 1 ).<Boolean>raw() );
    }

}
