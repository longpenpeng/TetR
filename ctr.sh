#!/bin/bash
#./ctr.sh /home/longpengpeng/refine_TETR/metarial /home/longpengpeng/refine_TETR/test/#test_mulitThread/statistic/program/class 4 6 /home/longpengpeng/refine_TETR/test/#test_mulitThread/4_6
function runPal(){
	infile=$1
	outfile=$2 
	gapLimit=6
	nummismatch=2
	minPallen=5
	maxPallen=20
	overlap=false
	palindrome $infile -minpallen=$minPallen -maxpallen=$maxPallen -gaplimit=$gapLimit -nummismatches=$nummismatch -outfile=$outfile -overlap=$overlap 1>&- 2>&-
}
function creatDir(){
    if [ ! -d $1 ];then
	mkdir $1
    fi 
}
function rmPalOut(){
    palOutDir=$1
    counter=0
    for file in $palOutDir/*
    do
      counter=$(($counter+1))
      rm $file
      if (( $counter >= 2950 ));then
         break;
      fi
    done
}
metarial=$1
wholeDBD="$1/wholeDBD"
accFile="$1/wholePalindromic/acc"
wholeUpstream="$1/wholeUpstream"
class=$2
minSetSize=$3
maxSetSize=$4
outDir=$5

setSizeError="$outDir/setSizeError"

creatDir $outDir
cd class/
for c in $(seq $minSetSize $maxSetSize)
do
  echo ">>>##############  `date +%H:%M:%S`  $c ###############"
  setSize=$c
  out="$outDir/$setSize"
  DBDir="$out/DBD"
  alignOutDir="$out/alignOut"
  upstreamOutDir="$out/upstreamOut"
  palOutDir="$out/palOut"
  creatDir $out
  creatDir $DBDir
  creatDir $alignOutDir
  creatDir $upstreamOutDir
  creatDir $palOutDir
  distr="$out/distr"
  java -Xmx2048m statistic_test.SelectDBDSet $wholeDBD $accFile $outDir $setSize 
  echo "   ############  `date +%H:%M:%S`    align begin ############  " 
  for file in $DBDir/*
  do  
     
     #echo "`basename $file`"
     alignOut="$alignOutDir/`basename $file`" 
     clustalw2 -infile=$file -outfile=$alignOut -output=clustal 1>/dev/null 2>&1 
  done
  echo "   ############  `date +%H:%M:%S`    align finish ############  "
  java -Xmx2048m statistic_test.Main $wholeUpstream $alignOutDir $upstreamOutDir $setSize $setSizeError 
  
  for file in $upstreamOutDir/*
  do
    pal="$palOutDir/`basename $file`"
    runPal $file  $pal 
  done
  echo "   ############  `date +%H:%M:%S`    pal finish ############  " 
  java -Xmx2048m  statistic_test.PalSearch $palOutDir $upstreamOutDir $distr 1>/dev/null 2>&1
  echo "################  `date +%H:%M:%S`  $c ###############"
  rm -r $DBDir $alignOutDir $upstreamOutDir 
  rmPalOut $palOutDir
done












