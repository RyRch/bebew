#!/bin/sh
clean()
{
	cd /tmp
	rm -rf $work_folder
}

work_folder="/tmp/bb42-$(shuf -i0-10000 -n 1)"
mkdir $work_folder
if [ ! $? -eq 0 ]; then
  exit 1
fi
cd $work_folder
echo "hehe"
direct_link=$(curl -L https://www.mediafire.com/file/ap8e39gam2p4agz/babyshark42.tar.gz/file | grep "download.*babyshark42.tar.gz" | sed 's/.*"\(.*\)"/\1/')
if [ -z $direct_link ]; then
  echo "An error occured during parsing (Link probably broken). Aborting..." 1>&2
  exit 1
fi
curl -L $direct_link -o babyshark42.tar.gz
if ! file babyshark42.tar.gz | grep "gzip compressed data" > /dev/null; then
	echo "An error occured during downloading (Link probably broken). Aborting..." 1>&2
	exit 1
fi
tar xf babyshark42.tar.gz
./babyshark &
bb42_pid=$!
./turn_up_the_radio.sh $bb42_pid 40
tail --pid=$bb42_pid -f /dev/null
clean
