/*
 * Copyright © 2017-2019 WireGuard LLC. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.wireguard.config;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Utility methods for creating instances of {@link InetAddress}.
 */
public final class InetAddresses {

    private InetAddresses() {
        // Prevent instantiation.
    }

    /**
     * Parses a numeric IPv4 or IPv6 address without performing any DNS lookups.
     *
     * @param address a string representing the IP address
     * @return an instance of {@link Inet4Address} or {@link Inet6Address}, as appropriate
     */
    public static InetAddress parse(final String address) throws ParseException {
        if (address.isEmpty())
            throw new ParseException(InetAddress.class, address, "Empty address");
        try {
            final String[] ips = address.split("\\.");
            byte byte1 = (byte)Integer.parseInt(ips[0]);
            byte byte2 = (byte)Integer.parseInt(ips[1]);
            byte byte3 = (byte)Integer.parseInt(ips[2]);
            byte byte4 = (byte)Integer.parseInt(ips[3]);
            InetAddress inetAddress = InetAddress.getByAddress(new byte[]{byte1,byte2,byte3,byte4});
            return inetAddress;
        } catch (final UnknownHostException e) {
            final Throwable cause = e.getCause();
            // Re-throw parsing exceptions with the original type, as callers might try to catch
            // them. On the other hand, callers cannot be expected to handle reflection failures.
            if (cause instanceof IllegalArgumentException)
                throw new ParseException(InetAddress.class, address, cause);
            throw new RuntimeException(e);
        }
    }
}
