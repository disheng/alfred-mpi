package it.uniroma3.dia.alfred.mpi.runner;

import it.uniroma3.dia.alfred.mpi.model.ConfigHolder;
import it.uniroma3.dia.alfred.mpi.model.serializer.ConfigHolderSerializable;
import it.uniroma3.dia.alfred.mpi.runner.MPIConstants.AbortReason;
import it.uniroma3.dia.alfred.mpi.runner.MPIConstants.TagValue;

import java.util.List;

import mpi.MPI;
import mpi.MPIException;

import com.google.common.collect.Lists;

public class MasterMPI {
	// TODO: contains master logic

	public static void run(List<ConfigHolder> inputConfigs, int processCountWithoutMaster) throws MPIException {
		List<Integer> confPerWorker = Lists.newArrayList();
		int atLeast = inputConfigs.size() / processCountWithoutMaster;
		int workRest = inputConfigs.size() % processCountWithoutMaster;
		
		System.out.println("Workload: " + inputConfigs.size() + "- PCount:" + processCountWithoutMaster + "- Min work: " +  atLeast + "- Rest: " + workRest);
		
		for(int i = 0; i < processCountWithoutMaster; ++i) {
			if (workRest != 0) {
				confPerWorker.add(atLeast + 1);
				workRest = workRest - 1;
			} else {
				confPerWorker.add(atLeast);
			}
		}
		
		System.out.println("Process[" + MPI.COMM_WORLD.Rank() + "]:send: " + confPerWorker);
		
		int[] messageSend = new int[1];
		int[] messageRecv = new int[1];
		messageSend[0] = 0;
		messageRecv[0] = 0;		
		
		for(int i = 0; i < processCountWithoutMaster; ++i) {
			messageSend[0] = confPerWorker.get(i);
			try {
				MPI.COMM_WORLD.Send(messageSend, 0, 1, MPI.INT, i + 1, TagValue.TAG_SIZE_CONF.getValue());
			} catch (MPIException e) {
				e.printStackTrace();
				RunAlfred.abort(AbortReason.WORK_SEND);
			}
		}
		
		messageSend[0] = 0;
		messageRecv[0] = 0;
		try {
			MPI.COMM_WORLD.Reduce(messageSend, 0, messageRecv, 0, 1, MPI.INT, MPI.SUM, MPIConstants.MASTER);
		} catch (MPIException e) {
			e.printStackTrace();
			RunAlfred.abort(AbortReason.WORK_SEND_ACK);
		}
		
		System.out.println("Process[" + MPI.COMM_WORLD.Rank() + "]:recv: " + messageRecv[0]);
		if (messageRecv[0] != inputConfigs.size()) {
			RunAlfred.abort(AbortReason.WORK_SEND_ACK);
		}
		
		int slaveId = 1;
		int confSent = 0;
		char[] localBuffer;
		for(Integer slaveSend: confPerWorker) {
			for(int i = confSent; i < slaveSend; ++i, ++confSent) {
				localBuffer = ConfigHolderSerializable.toJson(inputConfigs.get(i)).toCharArray();
				messageSend[0] = localBuffer.length;
				
				MPI.COMM_WORLD.Send(messageSend, 0, 1, MPI.INT, slaveId, TagValue.TAG_CONF_LEN.getValue());
				MPI.COMM_WORLD.Send(localBuffer, 0, localBuffer.length, MPI.CHAR, slaveId, TagValue.TAG_CONF_DATA.getValue());
			}
			
			slaveId++;
		}
		
		MPI.COMM_WORLD.Barrier();
		// TODO: Recv boolean results
	}

}
