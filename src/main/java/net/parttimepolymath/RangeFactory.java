package net.parttimepolymath;

import net.jcip.annotations.ThreadSafe;
import net.parttimepolymath.iplib.IPRange;
import net.parttimepolymath.iplib.Ranges;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Thread safe way of limiting the number of IPRange instances get built.
 */
@ThreadSafe
public class RangeFactory {
    private static AtomicReference<Ranges> holder = new AtomicReference<Ranges>();

    public static Ranges getRanges() throws IOException, InterruptedException {
        Ranges ranges = holder.get();
        if (ranges == null) {
            ranges =  new IPRange();
            holder.compareAndSet(null, ranges);
        }
        return ranges;
    }
}
