/*
 * Copyright 2015 Open Networking Laboratory
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onosproject.provider.tunnel.cli;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Optional;

import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.onlab.packet.IpAddress;
import org.onosproject.cli.AbstractShellCommand;
import org.onosproject.core.DefaultGroupId;
import org.onosproject.net.DefaultAnnotations;
import org.onosproject.net.DeviceId;
import org.onosproject.net.PortNumber;
import org.onosproject.net.SparseAnnotations;
import org.onosproject.net.provider.ProviderId;
import org.onosproject.net.tunnel.DefaultOpticalTunnelEndPoint;
import org.onosproject.net.tunnel.DefaultTunnelDescription;
import org.onosproject.net.tunnel.IpTunnelEndPoint;
import org.onosproject.net.tunnel.OpticalLogicId;
import org.onosproject.net.tunnel.OpticalTunnelEndPoint;
import org.onosproject.net.tunnel.Tunnel;
import org.onosproject.net.tunnel.TunnelDescription;
import org.onosproject.net.tunnel.TunnelEndPoint;
import org.onosproject.net.tunnel.TunnelId;
import org.onosproject.net.tunnel.TunnelName;
import org.onosproject.net.tunnel.TunnelProvider;

/**
 * Supports for creating a tunnel by using IP address and optical as tunnel end
 * point.
 */
@Command(scope = "onos", name = "create-tunnels",
description = "Supports for creating a tunnel by using IP address and optical as tunnel end point now.")
public class CreateTunnelCommand extends AbstractShellCommand {

    @Argument(index = 0, name = "src", description = "Source tunnel point."
            + " Only supports for IpTunnelEndPoint and OpticalTunnelEndPoint as end point now."
            + " If creates a ODUK or OCH type tunnel, the formatter of this argument is DeviceId-PortNumber."
            + " Otherwise src means IP address.", required = true, multiValued = false)
    String src = null;

    @Argument(index = 1, name = "dst", description = "Destination tunnel point."
            + " Only supports for IpTunnelEndPoint and OpticalTunnelEndPoint as end point now."
            + " If creates a ODUK or OCH type tunnel, the formatter of this argument is DeviceId-PortNumber."
            + " Otherwise dst means IP address.", required = true, multiValued = false)
    String dst = null;
    @Argument(index = 2, name = "type", description = "The type of tunnels,"
            + " It includes MPLS, VLAN, VXLAN, GRE, ODUK, OCH", required = true, multiValued = false)
    String type = null;
    @Argument(index = 3, name = "groupId",
            description = "Group flow table id which a tunnel match up", required = true, multiValued = false)
    String groupId = null;

    @Argument(index = 4, name = "tunnelName",
            description = "The name of tunnels", required = false, multiValued = false)
    String tunnelName = null;

    @Argument(index = 5, name = "bandWith",
            description = "The bandWith attribute of tunnel", required = false, multiValued = false)
    String bandWith = null;

    private static final String FMT = "The tunnel identity is %s";

    @Override
    protected void execute() {
        TunnelProvider service = get(TunnelProvider.class);
        ProviderId producerName = new ProviderId("default",
                                                 "org.onosproject.provider.tunnel.default");
        TunnelEndPoint srcPoint = null;
        TunnelEndPoint dstPoint = null;
        Tunnel.Type trueType = null;
        if ("MPLS".equals(type)) {
            trueType = Tunnel.Type.MPLS;
            srcPoint = IpTunnelEndPoint.ipTunnelPoint(IpAddress.valueOf(src));
            dstPoint = IpTunnelEndPoint.ipTunnelPoint(IpAddress.valueOf(dst));
        } else if ("VLAN".equals(type)) {
            trueType = Tunnel.Type.VLAN;
            srcPoint = IpTunnelEndPoint.ipTunnelPoint(IpAddress.valueOf(src));
            dstPoint = IpTunnelEndPoint.ipTunnelPoint(IpAddress.valueOf(dst));
        } else if ("VXLAN".equals(type)) {
            trueType = Tunnel.Type.VXLAN;
            srcPoint = IpTunnelEndPoint.ipTunnelPoint(IpAddress.valueOf(src));
            dstPoint = IpTunnelEndPoint.ipTunnelPoint(IpAddress.valueOf(dst));
        } else if ("GRE".equals(type)) {
            trueType = Tunnel.Type.GRE;
            srcPoint = IpTunnelEndPoint.ipTunnelPoint(IpAddress.valueOf(src));
            dstPoint = IpTunnelEndPoint.ipTunnelPoint(IpAddress.valueOf(dst));
        } else if ("ODUK".equals(type)) {
            trueType = Tunnel.Type.ODUK;
            String[] srcArray = src.split("||");
            checkArgument(srcArray.length < 2, "Illegal src formatter.");
            String[] dstArray = dst.split("||");
            checkArgument(dstArray.length < 2, "Illegal dst formatter.");
            srcPoint = new DefaultOpticalTunnelEndPoint(
                                                        producerName,
                                                        Optional.of(DeviceId
                                                                .deviceId(srcArray[0])),
                                                        Optional.of(PortNumber
                                                                .portNumber(srcArray[1])),
                                                        null,
                                                        OpticalTunnelEndPoint.Type.LAMBDA,
                                                        OpticalLogicId
                                                                .logicId(0),
                                                        true);
            dstPoint = new DefaultOpticalTunnelEndPoint(
                                                        producerName,
                                                        Optional.of(DeviceId
                                                                .deviceId(dstArray[0])),
                                                        Optional.of(PortNumber
                                                                .portNumber(dstArray[1])),
                                                        null,
                                                        OpticalTunnelEndPoint.Type.LAMBDA,
                                                        OpticalLogicId
                                                                .logicId(0),
                                                        true);
        } else if ("OCH".equals(type)) {
            trueType = Tunnel.Type.OCH;
            String[] srcArray = src.split("-");
            String[] dstArray = dst.split("-");
            srcPoint = new DefaultOpticalTunnelEndPoint(
                                                        producerName,
                                                        Optional.of(DeviceId
                                                                .deviceId(srcArray[0])),
                                                        Optional.of(PortNumber
                                                                .portNumber(srcArray[1])),
                                                        null,
                                                        OpticalTunnelEndPoint.Type.LAMBDA,
                                                        OpticalLogicId
                                                                .logicId(0),
                                                        true);
            dstPoint = new DefaultOpticalTunnelEndPoint(
                                                        producerName,
                                                        Optional.of(DeviceId
                                                                .deviceId(dstArray[0])),
                                                        Optional.of(PortNumber
                                                                .portNumber(dstArray[1])),
                                                        null,
                                                        OpticalTunnelEndPoint.Type.LAMBDA,
                                                        OpticalLogicId
                                                                .logicId(0),
                                                        true);
        } else {
            print("Illegal tunnel type. Please input MPLS, VLAN, VXLAN, GRE, ODUK or OCH.");
            return;
        }

        SparseAnnotations annotations = DefaultAnnotations
                .builder()
                .set("bandWith", bandWith == null && "".equals(bandWith) ? "0" : bandWith)
                .build();
        TunnelDescription tunnel = new DefaultTunnelDescription(
                                                                null,
                                                                srcPoint,
                                                                dstPoint,
                                                                trueType,
                                                                new DefaultGroupId(
                                                                                   Integer.valueOf(groupId)
                                                                                           .intValue()),
                                                                producerName,
                                                                TunnelName
                                                                        .tunnelName(tunnelName),
                                                                annotations);
        TunnelId tunnelId = service.tunnelAdded(tunnel);
        print(FMT, tunnelId.id());
    }

}
