/*
 * Copyright © 2015 Intracom Telecom and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package eu.virtuwind.monitoring.impl;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.CheckedFuture;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.ReadOnlyTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.controller.md.sal.common.api.data.ReadFailedException;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.NetworkTopology;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.TopologyId;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.Topology;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.TopologyKey;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class NodeMonitor {

	private static final Logger LOG = LoggerFactory
			.getLogger(NodeMonitor.class);

	public DataBroker db;

	public NodeMonitor(DataBroker db) {
		this.db = db;
	}


	public static List<org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.topology.Node> getAllNodes(DataBroker db) {
		List<org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.topology.Node> nodeList = new ArrayList<>();

		try {
			//Topology Id
			TopologyId topoId = new TopologyId("flow:1");
			//get the InstanceIdentifier
			InstanceIdentifier<Topology> nodesIid = InstanceIdentifier.builder(NetworkTopology.class).child(Topology.class, new TopologyKey(topoId)).toInstance();
			ReadOnlyTransaction nodesTransaction = db.newReadOnlyTransaction();

			//Read from operational database
			CheckedFuture<Optional<Topology>, ReadFailedException> nodesFuture = nodesTransaction
					.read(LogicalDatastoreType.OPERATIONAL, nodesIid);
			Optional<Topology> nodesOptional = nodesFuture.checkedGet();

			if (nodesOptional != null && nodesOptional.isPresent()) {
				nodeList = nodesOptional.get().getNode();
			}
			return nodeList;
		} catch (Exception e) {
			LOG.info("Node Fetching Failed");
			return nodeList;
		}

	}
//TODO: IMPLEMENT THE METHOD TO GET ALL LINKS
	public static List<org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.topology.Link> getAllLinks(DataBroker db) {
		List<org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.topology.Link> linkList = new ArrayList<>();
		try {
			//Topology Id
			TopologyId topoId = new TopologyId("flow:1");
			//get the InstanceIdentifier
			InstanceIdentifier<Topology> linksIid = InstanceIdentifier.builder(NetworkTopology.class).child(Topology.class, new TopologyKey(topoId)).toInstance();
			ReadOnlyTransaction nodesTransaction = db.newReadOnlyTransaction();

			//Read from operational database
			CheckedFuture<Optional<Topology>, ReadFailedException> linksFuture = nodesTransaction
					.read(LogicalDatastoreType.OPERATIONAL, linksIid);
			Optional<Topology> linksOptional = linksFuture.checkedGet();

			if (linksOptional != null && linksOptional.isPresent()) {
				linkList = linksOptional.get().getLink();
			}
			return linkList;
		} catch (Exception e) {
			LOG.info("Link Fetching Failed");
			return linkList;
		}
	}

}